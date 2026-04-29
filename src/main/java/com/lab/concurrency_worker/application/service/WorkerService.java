package com.lab.concurrency_worker.application.service;

import com.lab.concurrency_worker.domain.model.InputEntity;
import com.lab.concurrency_worker.domain.model.ResultEntity;
import com.lab.concurrency_worker.domain.repository.InputRepository;
import com.lab.concurrency_worker.domain.repository.ResultRepository;
import com.lab.concurrency_worker.infrastructure.filesystem.FileLogger;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.nio.file.*;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final InputRepository inputRepository;
    private final ResultRepository resultRepository;
    private final FileLogger fileLogger;

    @Transactional
    public int processBatch(String workerId, int batchSize) {

        List<InputEntity> inputs = inputRepository.findBatchForUpdate(batchSize);

        if (inputs.isEmpty()) {
            return 0;
        }

        // Marcar como en proceso
        inputs.forEach(input -> input.setStatus("in_process"));

        // Crear resultados en memoria
        List<ResultEntity> results = inputs.stream().map(input -> {

            String processed = processData(input.getDescription());

            ResultEntity result = new ResultEntity();
            result.setInputId(input.getId());
            result.setWorkerIdentifier(workerId);
            result.setResult(processed);
            result.setDate(LocalDateTime.now());

            // Escritura en archivo compartido
            fileLogger.log(workerId, input.getId());

            return result;

        }).toList();

        // Guardado en lote
        resultRepository.saveAll(results);

        // Marcar como procesados
        inputs.forEach(input -> input.setStatus("processed"));

        return inputs.size();
    }

    private String processData(String data) {
        return "processed: " + data;
    }
}

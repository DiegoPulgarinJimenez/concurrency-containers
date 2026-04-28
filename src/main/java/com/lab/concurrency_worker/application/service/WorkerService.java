package com.lab.concurrency_worker.application.service;

import com.lab.concurrency_worker.domain.model.InputEntity;
import com.lab.concurrency_worker.domain.model.ResultEntity;
import com.lab.concurrency_worker.domain.repository.InputRepository;
import com.lab.concurrency_worker.domain.repository.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final InputRepository inputRepository;
    private final ResultRepository resultRepository;

    @Transactional
    public int processBatch(String workerId, int batchSize) {

        var inputs = inputRepository.findBatchForUpdate(batchSize);

        if (inputs.isEmpty()) {
            return 0;
        }

        // Marcar como en proceso
        inputs.forEach(input -> input.setStatus("in_process"));

        // Crear resultados en memoria
        var results = inputs.stream().map(input -> {
            ResultEntity result = new ResultEntity();
            result.setInputId(input.getId());
            result.setWorkerIdentifier(workerId);
            result.setResult(processData(input.getDescription()));
            result.setDate(LocalDateTime.now());
            return result;
        }).toList();

        resultRepository.saveAll(results);

        // Marcar como procesados
        inputs.forEach(input -> input.setStatus("processed"));

        return inputs.size();
    }

    private String processData(String data) {
        return "processed: " + data;
    }
}

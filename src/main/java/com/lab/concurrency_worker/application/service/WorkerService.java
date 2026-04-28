package com.lab.concurrency_worker.application.service;

import com.lab.concurrency_worker.domain.model.InputEntity;
import com.lab.concurrency_worker.domain.model.ResultEntity;
import com.lab.concurrency_worker.domain.repository.InputRepository;
import com.lab.concurrency_worker.domain.repository.ResultRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final InputRepository inputRepository;
    private final ResultRepository resultRepository;

    @Transactional
    public boolean processNext(String workerId) {

        Optional<InputEntity> optionalInput = inputRepository.findNextPendingForUpdate();

        if (optionalInput.isEmpty()) {
            return false;
        }

        InputEntity input = optionalInput.get();

        // Marcar como en proceso
        input.setStatus("in_process");
        inputRepository.save(input);

        // Simula procesamiento
        String processedResult = processData(input.getDescription());

        // Guardar resultado
        ResultEntity result = new ResultEntity();
        result.setInputId(input.getId());
        result.setWorkerIdentifier(workerId);
        result.setResult(processedResult);
        result.setDate(LocalDateTime.now());

        resultRepository.save(result);

        // Marcar como procesado
        input.setStatus("processed");
        inputRepository.save(input);

        return true;
    }

    private String processData(String data) {
        /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }*/
        return "processed: " + data;
    }
}

package com.lab.concurrency_worker.worker;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import com.lab.concurrency_worker.application.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;

@Component
@RequiredArgsConstructor
public class WorkerRunner implements CommandLineRunner {

    private final WorkerService workerService;

    @Override
    public void run(String @NonNull ... args) throws UnknownHostException {

        // String workerId = System.getenv().getOrDefault("WORKER_ID", "worker-default");
        String fullName = java.net.InetAddress.getLocalHost().getHostName();
        String workerId = fullName.substring(fullName.lastIndexOf("-") + 1);
        workerId = "worker-" + workerId;

        while (true) {

            int processed = workerService.processBatch(workerId, 20);

            if (processed == 0) {
                System.out.println(workerId + " - No hay más tareas. Esperando...");

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

            } else {
                System.out.println(workerId + " - Procesó " + processed + " tareas en lote");
            }
        }
    }
}

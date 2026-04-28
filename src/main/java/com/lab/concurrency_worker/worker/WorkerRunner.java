package com.lab.concurrency_worker.worker;

import org.springframework.boot.CommandLineRunner;
import com.lab.concurrency_worker.application.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerRunner implements CommandLineRunner {

    private final WorkerService workerService;

    @Override
    public void run(String... args) throws Exception {

        String workerId = System.getenv().getOrDefault("WORKER_ID", "worker-default");

        while (true) {
            boolean processed = workerService.processNext(workerId);

            if (!processed) {
                System.out.println(workerId + " - No hay más tareas. Esperando...");
                Thread.sleep(2000);
            } else {
                System.out.println(workerId + " - Procesó una tarea");
            }
        }
    }
}

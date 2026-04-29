package com.lab.concurrency_worker.infrastructure.filesystem;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;

@Component
public class FileLogger {

    public void log(String workerId, Long inputId) {
        try {
            Files.writeString(
                    Path.of("/data/log.txt"),
                    workerId + " procesó input " + inputId + "\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

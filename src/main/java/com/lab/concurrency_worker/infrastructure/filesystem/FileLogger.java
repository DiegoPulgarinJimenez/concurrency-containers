package com.lab.concurrency_worker.infrastructure.filesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@Component
public class FileLogger {

    private static final Logger log = LoggerFactory.getLogger(FileLogger.class);
    private static final Path PATH = Path.of("/data/log.txt");

    public void log(String workerId, Long inputId) {
        String texto = workerId + " procesó input " + inputId + "\n";

        try (FileChannel channel = FileChannel.open(PATH,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                StandardOpenOption.APPEND)) {

            try (var ignored = channel.lock()) {

                ByteBuffer buffer = ByteBuffer.wrap(texto.getBytes());

                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }

        } catch (IOException e) {
            log.error("Error escribiendo en /data/log.txt", e);
        }
    }
}
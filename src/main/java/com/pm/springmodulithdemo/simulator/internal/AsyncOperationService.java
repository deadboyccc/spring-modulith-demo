package com.pm.springmodulithdemo.simulator.internal;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class AsyncOperationService implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(AsyncOperationService.class);

    @Override
    public void run(String @NonNull ... args) {
        log.info("Launching async operations...");

        for (var i = 1; i <= 5; i++) {
            executeAsyncTask("Task-" + i);
        }
    }

    @Async
    public CompletableFuture<String> executeAsyncTask(String taskName) {
        var thread = Thread.currentThread();

        log.info(
                "Started [{}] | thread={} | virtual={}",
                taskName,
                thread.getName(),
                thread.isVirtual()
        );

        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            log.warn("Task [{}] was interrupted", taskName);
            return CompletableFuture.failedFuture(e);
        }

        var result = "Result of " + taskName;

        log.info(
                "Completed [{}] | thread={}",
                taskName,
                thread.getName()
        );

        return CompletableFuture.completedFuture(result);
    }
}

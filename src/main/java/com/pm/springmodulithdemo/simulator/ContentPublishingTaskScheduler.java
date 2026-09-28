package com.pm.springmodulithdemo.simulator;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ContentPublishingTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ContentPublishingTaskScheduler.class);
    private static final int REQUEST_COUNT = 3;

    private final RestClient restClient;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public ContentPublishingTaskScheduler() {
        this.restClient = RestClient.builder().baseUrl("http://localhost:8080").build();
    }

    @EventListener(ApplicationReadyEvent.class)
    void start() {
        beginRequest(1);
    }

    private void beginRequest(int requestNumber) {
        logger.info("Sending request {} of {}...", requestNumber, REQUEST_COUNT);
        // schedule the request to be sent after 1 second
        scheduler.schedule(() -> sendRequest(requestNumber), 1, TimeUnit.SECONDS);
    }

    private void sendRequest(int requestNumber) {
        try {
            restClient.post()
                    .uri("/api/content")
                    .body(new PublishContentRequest(
                            "Spring Modulith Post " + requestNumber,
                            "https://example.com/demo-" + requestNumber,
                            "BLOG_POST"
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (RuntimeException exception) {
            logger.error("Request {} of {} failed.", requestNumber, REQUEST_COUNT, exception);
        }

        if (requestNumber < REQUEST_COUNT) {
            logger.info("Waiting for 5 seconds...");
            scheduler.schedule(() -> beginRequest(requestNumber + 1), 5, TimeUnit.SECONDS);
        } else {
            logger.info("Finished sending all {} requests!", REQUEST_COUNT);
        }
    }

    @PreDestroy
    void shutdown() {
        scheduler.shutdown();
    }

    private record PublishContentRequest(String title, String url, String type) {
    }
}

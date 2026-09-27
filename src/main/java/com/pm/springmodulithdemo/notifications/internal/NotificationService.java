package com.pm.springmodulithdemo.notifications.internal;

import com.pm.springmodulithdemo.publishing.ContentPublished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final SubscriberRepository subscribers;
    private final boolean simulateFailure;

    public NotificationService(SubscriberRepository subscribers,
                               @Value("${app.notifications.simulate-failure:false}") boolean simulateFailure) {
        this.subscribers = subscribers;
        this.simulateFailure = simulateFailure;
    }

    @ApplicationModuleListener
    public void onContentPublished(ContentPublished event) {
        var content = event.content();
        if (simulateFailure) {
            throw new RuntimeException("Simulated notification failure for: " + content.title());
        }
        subscribers.findAll().forEach(subscriber ->
                log.info("Notifying {} about: {}", subscriber.email(), content.title()));
    }
}

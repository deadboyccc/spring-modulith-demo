package com.pm.springmodulithdemo.publishing.internal;

import com.pm.springmodulithdemo.publishing.Content;
import com.pm.springmodulithdemo.publishing.ContentPublished;
import com.pm.springmodulithdemo.publishing.ContentType;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PublishingService {

    private final ContentRepository contentRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PublishingService(ContentRepository contentRepository, ApplicationEventPublisher eventPublisher) {
        this.contentRepository = contentRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Content publish(String title, String url, ContentType type) {
        var content = contentRepository.save(Content.draft(title, url, type));
        // Publish the saved instance so listeners receive its database-assigned id.
        eventPublisher.publishEvent(new ContentPublished(content));
        return content;
    }

    public Optional<Content> findById(Long id) {
        return contentRepository.findById(id);
    }

    public List<Content> findAll() {
        return contentRepository.findAll();
    }
}

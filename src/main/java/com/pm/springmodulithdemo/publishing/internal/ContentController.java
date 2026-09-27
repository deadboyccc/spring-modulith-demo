package com.pm.springmodulithdemo.publishing.internal;

import com.pm.springmodulithdemo.publishing.Content;
import com.pm.springmodulithdemo.publishing.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ContentController {

    private final PublishingService publishing;

    public ContentController(PublishingService publishing) {
        this.publishing = publishing;
    }

    @GetMapping
    List<Content> findAll() {
        return publishing.findAll();
    }

    @GetMapping("/{id}")
    ResponseEntity<Content> findById(@PathVariable Long id) {
        return publishing.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<Content> publish(@RequestBody PublishContentRequest request) {
        var content = publishing.publish(request.title(), request.url(), request.type());
        return ResponseEntity
                .created(URI.create("/api/content/" + content.id()))
                .body(content);
    }

    record PublishContentRequest(String title, String url, ContentType type) {
    }
}

package com.shir.compsecurity.service;

import com.shir.compsecurity.entity.PostEntity;
import com.shir.compsecurity.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository repo;
    private final HtmlSanitizerService sanitizer;

    public List<PostEntity> findAll() {
        return repo.findAll();
    }

    public PostEntity create(String authorUsername, String title, String content) {
        String safeTitle = sanitizer.sanitize(title);
        String safeContent = sanitizer.sanitize(content);

        var entity = PostEntity.builder()
                .authorUsername(authorUsername)
                .title(safeTitle)
                .content(safeContent)
                .createdAt(Instant.now())
                .build();
        return repo.save(entity);
    }
}

package com.shir.compsecurity.controller;

import com.shir.compsecurity.dto.CreatePostRequest;
import com.shir.compsecurity.entity.PostEntity;
import com.shir.compsecurity.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    private final PostService postService;

    @GetMapping("/ping")
    public String ping() { return "pong"; }

    @GetMapping("/data")
    public List<PostEntity> getData() {
        return postService.findAll();
    }

    @PostMapping("/data")
    public PostEntity create(@Validated @RequestBody CreatePostRequest req, Principal principal) {
        String username = principal != null ? principal.getName() : "unknown";
        return postService.create(username, req.getTitle(), req.getContent());
    }
}

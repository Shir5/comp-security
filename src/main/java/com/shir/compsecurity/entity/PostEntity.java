package com.shir.compsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "posts")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PostEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=100)
    private String authorUsername;

    @Column(nullable=false, length=200)
    private String title;

    @Column(nullable=false, length=2000)
    private String content;

    @Column(nullable=false)
    private Instant createdAt;
}

package com.shir.compsecurity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=100)
    private String username;

    @Column(name="password_hash", nullable=false, length=200)
    private String passwordHash;

    @Column(nullable=false, length=30)
    private String role;

    @Column(nullable=false)
    private Instant createdAt;
}

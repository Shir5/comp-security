package com.shir.compsecurity.service;

import com.shir.compsecurity.entity.UserAccount;
import com.shir.compsecurity.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountRepository repo;
    private final PasswordEncoder encoder;

    @PostConstruct
    void seed() {
        if (!repo.existsByUsername("user")) {
            repo.save(UserAccount.builder()
                    .username("user").passwordHash(encoder.encode("password"))
                    .role("ROLE_USER").createdAt(Instant.now()).build());
        }
        if (!repo.existsByUsername("admin")) {
            repo.save(UserAccount.builder()
                    .username("admin").passwordHash(encoder.encode("password"))
                    .role("ROLE_ADMIN").createdAt(Instant.now()).build());
        }
        log.info("Users in DB: {}", repo.count());
    }

    public boolean exists(String username) { return repo.existsByUsername(username); }

    public UserAccount createUser(String username, String rawPassword) {
        var entity = UserAccount.builder()
                .username(username)
                .passwordHash(encoder.encode(rawPassword))
                .role("ROLE_USER")
                .createdAt(Instant.now())
                .build();
        return repo.save(entity);
    }

    public UserDetails toUserDetails(UserAccount ua) {
        return User.withUsername(ua.getUsername())
                .password(ua.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(ua.getRole())))
                .build();
    }

    public UserAccount findEntityByUsernameOrNull(String username) {
        return repo.findByUsername(username).orElse(null);
    }
}

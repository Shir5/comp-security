package com.shir.compsecurity.controller;

import com.shir.compsecurity.dto.*;
import com.shir.compsecurity.security.JwtUtil;
import com.shir.compsecurity.service.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UserAccountService accounts;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );
        UserDetails principal = (UserDetails) auth.getPrincipal();
        return JwtResponse.builder()
                .token(jwtUtil.generateToken(principal.getUsername()))
                .build();
    }

    @PostMapping("/register")
    public JwtResponse register(@Validated @RequestBody RegisterRequest req) {
        if (accounts.exists(req.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        accounts.createUser(req.getUsername(), req.getPassword());
        return JwtResponse.builder()
                .token(jwtUtil.generateToken(req.getUsername()))
                .build();
    }
}

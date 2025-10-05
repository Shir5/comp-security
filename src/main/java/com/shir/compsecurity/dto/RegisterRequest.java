package com.shir.compsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    @NotBlank @Size(min=3, max=100)
    private String username;

    @NotBlank @Size(min=6, max=128)
    private String password;
}

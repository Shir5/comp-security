package com.shir.compsecurity.dto;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JwtResponse {
    private String token;
    @Builder.Default
    private String tokenType = "Bearer";
}

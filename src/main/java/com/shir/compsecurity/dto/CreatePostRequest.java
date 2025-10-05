package com.shir.compsecurity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class CreatePostRequest {
    @NotBlank @Size(max=200)
    private String title;

    @NotBlank @Size(max=2000)
    private String content;
}

package com.example.blog.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long postId;
    @NotBlank
    private String content;
}

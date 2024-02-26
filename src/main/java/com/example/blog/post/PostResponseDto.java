package com.example.blog.post;

import com.example.blog.comment.CommentResponseDto;
import com.example.blog.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private User user;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> comments; // 댓글 리스트 추가
    private List<String> fileUrls; // 파일 URL 리스트 추가




    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.user = post.getUser();
        this.contents = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
        // 댓글 리스트가 null이 아닌 경우에만 스트림 처리
        this.comments = post.getComments() != null ? post.getComments().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()) : Collections.emptyList(); // 댓글 리스트 매핑
        this.fileUrls = post.getFileUrls();
    }
}

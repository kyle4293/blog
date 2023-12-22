package com.example.blog.comment;

import com.example.blog.user.User;
import com.example.blog.user.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public CommentResponseDto addComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody CommentRequestDto requestDto) {
        User user = userDetails.getUser();
        return commentService.addComment(user, requestDto);
    }

    @PutMapping("/{id}")
    public CommentResponseDto updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody CommentRequestDto requestDto) {
        User user = userDetails.getUser();
        return commentService.updateComment(id, user, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        User user = userDetails.getUser();
        commentService.deleteComment(id, user);
    }
}

package com.example.blog.comment;

import com.example.blog.exception.CustomException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.post.Post;
import com.example.blog.post.PostService;
import com.example.blog.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;

    public CommentResponseDto addComment(User user, CommentRequestDto requestDto) {
        Post post = postService.getPost(requestDto.getPostId());
        Comment comment = new Comment(user, post, requestDto);
        Comment saveComment = commentRepository.save(comment);

        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

    @Transactional
    public CommentResponseDto updateComment(Long id, User user, CommentRequestDto requestDto) {
        Comment comment = getComment(id, user);
        comment.update(requestDto);

        CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
        return commentResponseDto;
    }

    public void deleteComment(Long id, User user) {
        Comment comment = getComment(id, user);
        commentRepository.delete(comment);
    }

    private Comment getComment(Long id, User user) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.INDEX_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return comment;
    }
}

package com.example.blog.post;

import com.example.blog.exception.CustomException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.user.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(User user, PostRequestDto requestDto) {
        // RequestDto -> Entity
        Post post = new Post(user, requestDto);
        // DB 저장
        Post savePost = postRepository.save(post);

        return new PostResponseDto(savePost);
    }

    public Page<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc, String keyword) {
        Sort sort = Sort.by(isAsc ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
//        System.out.println("keyword = " + keyword);

        if (keyword != null && !keyword.isEmpty()) {
            QPost qPost = QPost.post;
            BooleanBuilder builder = new BooleanBuilder();
            builder.and(qPost.title.containsIgnoreCase(keyword)
                    .or(qPost.content.containsIgnoreCase(keyword)));
            return postRepository.findAll(builder, pageable).map(PostResponseDto::new);
        } else {
            return postRepository.findAll(pageable).map(PostResponseDto::new);
        }
    }

    public long getTotalPosts() {
        return postRepository.count();
    }

    public PostResponseDto getPostDto(Long id) {
        Post post = findPost(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.INDEX_NOT_FOUND));
    }

    @Transactional
    public Long updatePost(User user, Long id, PostRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);
        if (post.getUser().getId().equals(user.getId())) {
            post.update(requestDto);
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        return id;
    }

    public void deletePost(User user, Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Post post = findPost(id);

        if (post.getUser().getId().equals(user.getId())) {
            postRepository.delete(post);
        } else {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.INDEX_NOT_FOUND)
        );
    }
}
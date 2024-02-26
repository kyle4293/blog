package com.example.blog.post;

import com.example.blog.exception.CustomException;
import com.example.blog.exception.ErrorCode;
import com.example.blog.user.User;
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


//    public List<PostResponseDto> getPosts() {
//        // DB 조회
//        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
//    }

    public Page<PostResponseDto> getPosts(int page, int size, String sortBy, boolean isAsc) {
        // 페이징 처리
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);


        Page<Post> postList = postRepository.findAll(pageable);

        return postList.map(PostResponseDto::new);
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
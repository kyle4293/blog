package com.example.blog.post;


import com.example.blog.user.User;
import com.example.blog.user.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostResponseDto createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
        User user = userDetails.getUser();
        return postService.createPost(user, requestDto);
    }

//    @GetMapping
//    public List<PostResponseDto> getPosts() {
//        return postService.getPosts();
//    }

    @GetMapping
    public Page<PostResponseDto> getPosts(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("sortBy") String sortBy,
            @RequestParam("isAsc") boolean isAsc) {
        return postService.getPosts(page-1, size, sortBy, isAsc);
    }


    @GetMapping("/{id}")
    public PostResponseDto getPostDto(@PathVariable Long id) {
        return postService.getPostDto(id);
    }

    @PutMapping("/{id}")
    public Long updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id, @RequestBody PostRequestDto requestDto) {
        User user = userDetails.getUser();
        return postService.updatePost(user, id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {
        User user = userDetails.getUser();
        postService.deletePost(user, id);
    }
}
package com.example.blog.post;


import com.example.blog.aws.FileUploadService;
import com.example.blog.user.User;
import com.example.blog.user.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final FileUploadService fileUploadService;

    public PostController(PostService postService, FileUploadService fileUploadService) {
        this.postService = postService;
        this.fileUploadService = fileUploadService;
    }

//    @PostMapping
//    public PostResponseDto createPost(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody PostRequestDto requestDto) {
//        User user = userDetails.getUser();
//        return postService.createPost(user, requestDto);
//    }
    @PostMapping(consumes = "multipart/form-data")
    public PostResponseDto createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @RequestPart("post") PostRequestDto requestDto,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {
        User user = userDetails.getUser();
        // 파일 업로드 로직을 처리하고 파일 URL 리스트를 requestDto에 설정하는 로직 필요
        List<String> fileUrls = null; // 파일 업로드 서비스를 통해 파일을 업로드하고 URL 리스트를 받습니다.
        if (files != null && !files.isEmpty()) {
            fileUrls = fileUploadService.uploadFiles(files); // 파일 업로드 서비스 호출
            requestDto.setFileUrls(fileUrls); // 업로드된 파일의 URL 리스트를 requestDto에 설정
        }
        return postService.createPost(user, requestDto);
    }

    @GetMapping("/total")
    public long getTotalPosts() {
        return postService.getTotalPosts();
    }

    @GetMapping
    public Page<PostResponseDto> getPosts(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "isAsc", defaultValue = "true") boolean isAsc) {
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
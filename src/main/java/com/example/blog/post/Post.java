package com.example.blog.post;

import com.example.blog.comment.Comment;
import com.example.blog.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments;

    // 파일 URL을 저장하는 리스트
    @ElementCollection
    @CollectionTable(name = "post_files", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "file_url")
    private List<String> fileUrls = new ArrayList<>();

    public Post(User user, PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.fileUrls = requestDto.getFileUrls();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.fileUrls = requestDto.getFileUrls();
    }
}

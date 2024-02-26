package com.example.blog.post;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private List<String> fileUrls; // 파일 URL 리스트 추가
}

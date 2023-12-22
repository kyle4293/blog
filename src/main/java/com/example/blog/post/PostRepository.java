package com.example.blog.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Query Methods: 메서드 이름으로 SQL을 생성(Select * ~)
//    List<Post> findAllByOrderByModifiedAtDesc();

    Page<Post> findAll(Pageable pageable);

}
package com.example.forum.repository;

import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Page<Post>> findByCategory(Category category, Pageable pageable);
}

package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Post createPost(Post post, User user, Category category) {
        post.setUser(user);
        post.setCategory(category);
        post.setAuthor(user.getUsername());
        return postRepository.save(post);
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> findPostsByCategoryName(String categoryName, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + categoryName));

        return postRepository.findByCategory(category, pageable)
                .orElseThrow(IllegalArgumentException::new);
    }

    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }
}

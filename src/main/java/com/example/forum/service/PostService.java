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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

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

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found: " + id));
    }

    public Page<Post> findPostsByCategoryName(String categoryName, Pageable pageable) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new NoSuchElementException("Category not found: " + categoryName));

        return postRepository.findByCategory(category, pageable);
    }

    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContaining(keyword, pageable);
    }

    @Transactional
    public void increasePostViewCount(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        post.increaseViewCount();
    }
}

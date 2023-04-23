package com.example.forum.service;

import com.example.forum.domain.category.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(Post post, User user, Category category) {
        post.setUser(user);
        post.setCategory(category);
        post.setAuthor(user.getUsername());
        return postRepository.save(post);
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}

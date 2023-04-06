package com.example.forum.service;

import com.example.forum.domain.Post;
import com.example.forum.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    void testCreatePost() {
        Post post = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        when(postRepository.save(any())).thenReturn(post);

        Post savedPost = postService.createPost(post, "testAuthor");

        assertThat(post).isEqualTo(savedPost);
    }
}

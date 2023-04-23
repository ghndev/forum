package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.repository.PostRepository;
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
        User user = User.builder()
                .email("test@test.com")
                .username("testUser")
                .password("password1")
                .role(Role.USER)
                .build();

        Post post = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        Category category = Category.builder()
                .name("test")
                .build();

        when(postRepository.save(any())).thenReturn(post);

        Post savedPost = postService.createPost(post, user, category);

        assertThat(post).isEqualTo(savedPost);
    }
}

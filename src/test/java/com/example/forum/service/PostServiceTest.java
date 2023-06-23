package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.Role;
import com.example.forum.domain.User;
import com.example.forum.repository.CategoryRepository;
import com.example.forum.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CategoryRepository categoryRepository;

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

    @Test
    void testFindAll() {
        Post postA = Post.builder()
                .title("testTitleA")
                .content("testContent")
                .build();

        Post postB = Post.builder()
                .title("testTitleB")
                .content("testContent")
                .build();

        List<Post> posts = Arrays.asList(postA, postB);
        Page<Post> postPage = new PageImpl<>(posts);

        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);

        Page<Post> result = postService.findAll(Pageable.unpaged());

        assertThat(2).isEqualTo(result.getContent().size());
        assertThat("testTitleA").isEqualTo(result.getContent().get(0).getTitle());
        assertThat("testTitleB").isEqualTo(result.getContent().get(1).getTitle());
    }

    @Test
    void testFindPostsByCategoryName() {
        Category category = Category.builder()
                .name("testCategory")
                .build();

        Post postA = Post.builder()
                .title("testTitleA")
                .build();

        Post postB = Post.builder()
                .title("testTitleB")
                .build();

        postA.setCategory(category);
        postB.setCategory(category);

        List<Post> posts = Arrays.asList(postA, postB);
        Page<Post> postPage = new PageImpl<>(posts);

        when(categoryRepository.findByName(anyString())).thenReturn(Optional.of(category));
        when(postRepository.findByCategory(any(Category.class), any(Pageable.class))).thenReturn(postPage);

        Page<Post> result = postService.findPostsByCategoryName("testCategory", Pageable.unpaged());

        assertThat(2).isEqualTo(result.getContent().size());
        assertThat("testTitleA").isEqualTo(result.getContent().get(0).getTitle());
        assertThat("testTitleB").isEqualTo(result.getContent().get(1).getTitle());
    }

    @Test
    void testSearchPosts() {
        Post postA = Post.builder()
                .title("testA")
                .build();

        Post postB = Post.builder()
                .title("testB")
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> expectedPage = new PageImpl<>(Arrays.asList(postA, postB), pageable, 2);

        when(postRepository.findByTitleContaining(anyString(), any(Pageable.class)))
                .thenReturn(expectedPage);

        String keyword = "test";
        Page<Post> actualPage = postService.searchPosts(keyword, pageable);

        assertThat(expectedPage).isEqualTo(actualPage);
    }

    @Test
    void testIncreasePostViewCount() {
        Long postId = 1L;
        Post post = Post.builder()
                .build();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        postService.increasePostViewCount(postId);

        assertThat(post.getViewCount()).isEqualTo(1);
    }
}

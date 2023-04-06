package com.example.forum.controller;

import com.example.forum.controller.dto.PostCreateRequest;
import com.example.forum.domain.Post;
import com.example.forum.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    private MockMvc mockMvc;

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserDetails userDetails = User.withUsername("testUser")
                .password(passwordEncoder.encode("password1"))
                .roles("USER")
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("classpath:templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(postController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void testPosts() throws Exception {
        Post postA = Post.builder()
                .title("testTitle")
                .content("testContent")
                .author("test")
                .build();

        Post postB = Post.builder()
                .title("testTitle")
                .content("testContent")
                .author("test")
                .build();

        List<Post> postList = Arrays.asList(postA, postB);

        when(postService.findAll()).thenReturn(postList);

        mockMvc.perform(get("/posts"))
                .andExpect(model().attributeExists("postResponseList"))
                .andExpect(view().name("posts"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/posts/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postCreateRequest"))
                .andExpect(view().name("post-create"));
    }

    @Test
    void testCreatePostWithValidRequest() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setTitle("testTitle");
        postCreateRequest.setContent("testContent");

        when(postService.createPost(any(Post.class), any(String.class))).thenReturn(null);

        mockMvc.perform(post("/posts/create")
                        .flashAttr("postCreateRequest", postCreateRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void testCreatePostWithInvalidRequest() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setTitle("");
        postCreateRequest.setContent("");

        mockMvc.perform(post("/posts/create")
                        .flashAttr("postCreateRequest", postCreateRequest))
                .andExpect(status().isOk())
                .andExpect(view().name("post-create"));
    }
}
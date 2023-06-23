package com.example.forum.controller;

import com.example.forum.controller.dto.PostCreateRequest;
import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.Role;
import com.example.forum.service.CategoryService;
import com.example.forum.service.PostService;
import com.example.forum.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void injectCommonMocks() {
        category = Category.builder()
                .name("testCategory")
                .build();

        lenient().when(categoryService.findByName(any(String.class))).thenReturn(category);
    }

    @Test
    @WithMockUser
    void testGetPosts() throws Exception {
        Post postA = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        Post postB = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        postA.setCategory(category);
        postB.setCategory(category);

        List<Post> postList = Arrays.asList(postA, postB);

        Page<Post> postPage = new PageImpl<>(postList);

        when(postService.findAll(any(Pageable.class))).thenReturn(postPage);

        mockMvc.perform(get("/posts"))
                .andExpect(model().attributeExists("postResponsePage"))
                .andExpect(view().name("posts"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetPostsByCategory() throws Exception {
        Post postA = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        Post postB = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        postA.setCategory(category);
        postB.setCategory(category);

        List<Post> posts = Arrays.asList(postA, postB);
        Page<Post> postPage = new PageImpl<>(posts);

        when(postService.findPostsByCategoryName(anyString(), any(Pageable.class))).thenReturn(postPage);

        mockMvc.perform(get("/posts/category/{categoryName}", category.getName()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postResponsePage"))
                .andExpect(view().name("posts"));
    }

    @Test
    @WithMockUser
    void testCreateForm() throws Exception {
        mockMvc.perform(get("/posts/create"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("postCreateRequest"))
                .andExpect(view().name("post-create"));
    }

    @Test
    @WithMockUser
    void testCreatePostWithValidRequest() throws Exception {
        com.example.forum.domain.User mockUser = com.example.forum.domain.User.builder()
                .email("test@test.com")
                .username("testUser")
                .password("password1")
                .role(Role.USER)
                .build();

        when(userService.findByUsername(any(String.class))).thenReturn(mockUser);

        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setId(1L);
        postCreateRequest.setTitle("testTitle");
        postCreateRequest.setCategoryName("test");
        postCreateRequest.setContent("testContent");
        Post post = postCreateRequest.toEntity();

        when(postService.createPost(any(Post.class), eq(mockUser), eq(category)))
                .thenReturn(post);

        mockMvc.perform(post("/posts/create")
                        .with(csrf())
                        .flashAttr("postCreateRequest", postCreateRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/" + 1L));
    }

    @Test
    @WithMockUser
    void testCreatePostWithInvalidRequest() throws Exception {
        PostCreateRequest postCreateRequest = new PostCreateRequest();
        postCreateRequest.setTitle("");
        postCreateRequest.setContent("");

        mockMvc.perform(post("/posts/create")
                        .with(csrf())
                        .flashAttr("postCreateRequest", postCreateRequest))
                .andExpect(status().isOk())
                .andExpect(view().name("post-create"));

    }

    @Test
    @WithMockUser
    void testSearchPosts() throws Exception {
        String keyword = "test";
        Post postA = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        Post postB = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        postA.setCategory(category);
        postB.setCategory(category);

        List<Post> postList = Arrays.asList(postA, postB);
        Page<Post> postPage = new PageImpl<>(postList);

        when(postService.searchPosts(eq(keyword), any(Pageable.class))).thenReturn(postPage);

        mockMvc.perform(get("/posts/search")
                        .param("keyword", keyword))
                .andExpect(model().attributeExists("postResponsePage"))
                .andExpect(model().attribute("keyword", keyword))
                .andExpect(view().name("posts"))
                .andExpect(status().isOk());

        verify(postService, times(1)).searchPosts(eq(keyword), any(Pageable.class));
    }

    @Test
    @WithMockUser
    void testViewPost() throws Exception {
        Post post = Post.builder()
                .title("testTitle")
                .content("testContent")
                .build();

        post.setCategory(category);

        Long postId = 1L;

        when(postService.findById(postId)).thenReturn(post);

        mockMvc.perform(get("/posts/" + postId))
                .andExpect(status().isOk())
                .andExpect(view().name("post-detail"))
                .andExpect(model().attributeExists("postResponse"));
    }

    @Test
    @WithMockUser
    public void viewPostTest() throws Exception {
        Long postId = 1L;
        Post post = Post.builder()
                .build();

        post.setCategory(category);

        when(postService.findById(postId)).thenReturn(post);

        mockMvc.perform(get("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("post-detail"));
    }
}
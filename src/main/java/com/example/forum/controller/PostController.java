package com.example.forum.controller;

import com.example.forum.controller.dto.PostCreateRequest;
import com.example.forum.controller.dto.PostResponse;
import com.example.forum.domain.Category;
import com.example.forum.domain.Post;
import com.example.forum.domain.User;
import com.example.forum.service.CategoryService;
import com.example.forum.service.PostService;
import com.example.forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/posts")
    public String getPosts(@SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Post> postPage = postService.findAll(pageable);
        Page<PostResponse> postResponsePage = toPostResponsePage(postPage);

        model.addAttribute("postResponsePage", postResponsePage);
        return "posts";
    }

    @GetMapping("/posts/search")
    public String searchPosts(@RequestParam("keyword") String keyword,
                              @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Post> postPage = postService.searchPosts(keyword, pageable);
        Page<PostResponse> postResponsePage = toPostResponsePage(postPage);

        model.addAttribute("postResponsePage", postResponsePage);
        model.addAttribute("keyword", keyword);
        return "posts";
    }

    @GetMapping("/posts/category/{categoryName}")
    public String getPostsByCategory(@PathVariable String categoryName,
                                     @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        Page<Post> postPage = postService.findPostsByCategoryName(categoryName, pageable);
        Page<PostResponse> postResponsePage = toPostResponsePage(postPage);

        model.addAttribute("postResponsePage", postResponsePage);
        return "posts";
    }

    @GetMapping("/posts/create")
    public String createForm(Model model) {
        model.addAttribute("postCreateRequest", new PostCreateRequest());
        return "post-create";
    }

    @PostMapping("/posts/create")
    public String createPost(@Validated @ModelAttribute PostCreateRequest postCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post-create";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userService.findByUsername(currentUserName);

        Category category = categoryService.findByName(postCreateRequest.getCategoryName());
        Post post = postService.createPost(postCreateRequest.toEntity(), user, category);

        return "redirect:/posts/" + post.getId();
    }

    @GetMapping("/posts/{postId}")
    public String viewPost(@PathVariable Long postId, HttpServletRequest request, HttpServletResponse response, Model model) {
        Post post = postService.increasePostViewCount(postId, request, response);
        PostResponse postResponse = PostResponse.fromEntity(post);

        model.addAttribute("postResponse", postResponse);
        return "post-detail";
    }

    private Page<PostResponse> toPostResponsePage(Page<Post> postPage) {
        return postPage.map(PostResponse::fromEntity);
    }
}

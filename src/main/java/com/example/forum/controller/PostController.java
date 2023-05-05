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

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CategoryService categoryService;

    @GetMapping("/posts")
    public String getPosts(Model model, @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.findAll(pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::fromEntity);

        model.addAttribute("postResponsePage", postResponsePage);
        return "posts";
    }

    @GetMapping("/posts/search")
    public String searchPosts(Model model, @RequestParam("keyword") String keyword,
                              @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.searchPosts(keyword, pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::fromEntity);

        model.addAttribute("postResponsePage", postResponsePage);
        model.addAttribute("keyword", keyword);
        return "posts";
    }

    @GetMapping("/posts/{categoryName}")
    public String getPostsByCategory(Model model, @PathVariable String categoryName,
                                     @SortDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Post> postPage = postService.findPostsByCategoryName(categoryName, pageable);
        Page<PostResponse> postResponsePage = postPage.map(PostResponse::fromEntity);

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
        postService.createPost(postCreateRequest.toEntity(), user, category);

        return "redirect:/posts";
    }
}

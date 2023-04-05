package com.example.forum.controller;

import com.example.forum.controller.dto.PostCreateRequest;
import com.example.forum.controller.dto.PostResponse;
import com.example.forum.domain.Post;
import com.example.forum.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public String posts(Model model) {
        List<Post> postList = postService.findAll();
        List<PostResponse> postResponseList = postList.stream()
                .map(PostResponse::fromEntity)
                .collect(Collectors.toList());

        model.addAttribute("postResponseList", postResponseList);
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
        postService.createPost(postCreateRequest.toEntity(), currentUserName);

        return "redirect:/posts";
    }
}

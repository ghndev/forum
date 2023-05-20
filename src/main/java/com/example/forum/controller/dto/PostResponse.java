package com.example.forum.controller.dto;

import com.example.forum.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostResponse {

    private Long id;
    private String title;
    private String categoryName;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private int viewCount;

    public static PostResponse fromEntity(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setCategoryName(post.getCategory().getDisplayName());
        postResponse.setContent(post.getContent());
        postResponse.setAuthor(post.getAuthor());
        postResponse.setCreatedDate(post.getCreatedDate());
        postResponse.setViewCount(post.getViewCount());

        return postResponse;
    }
}

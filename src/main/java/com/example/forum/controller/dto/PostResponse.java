package com.example.forum.controller.dto;

import com.example.forum.domain.Post;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;

    private LocalDateTime dateTime;

    private int views;

    public static PostResponse fromEntity(Post post) {
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setAuthor(post.getAuthor());
        postResponse.setDateTime(post.getCreated());

        return postResponse;
    }
}

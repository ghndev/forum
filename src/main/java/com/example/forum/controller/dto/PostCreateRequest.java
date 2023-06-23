package com.example.forum.controller.dto;

import com.example.forum.domain.Post;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class PostCreateRequest {

    private Long id;

    @NotBlank
    @Length(min = 1, max = 20)
    private String title;

    @NotBlank
    private String categoryName;

    @NotBlank
    private String content;

    public Post toEntity() {
        return Post.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .build();
    }
}

package com.example.forum.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String author;

    private int views;

    @Builder
    public Post(String title, String content, String author, int views) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.views = views;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

package com.example.wanted.domain;

import com.example.wanted.request.PostModify;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long Id;

    private String title;

    private String content;

    @ManyToOne
    private User user;

    @Builder
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }


    public void modify(PostModify postModify) {

        this.title = postModify.getTitle();
        this.content = postModify.getContent();

    }



}

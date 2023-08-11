package com.example.wanted.response;

import com.example.wanted.domain.Post;
import lombok.Getter;

@Getter
public class PostRead {

    private String title;

    private String content;

    private String email;


    public PostRead(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.email = post.getUser().getEmail();
    }
}

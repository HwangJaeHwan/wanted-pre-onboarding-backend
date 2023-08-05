package com.example.wanted.response;

import com.example.wanted.domain.Post;
import lombok.Getter;

@Getter
public class PostListInfo {

    private Long id;

    private String title;

    private String email;

    public PostListInfo(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.email = post.getUser().getEmail();
    }
}

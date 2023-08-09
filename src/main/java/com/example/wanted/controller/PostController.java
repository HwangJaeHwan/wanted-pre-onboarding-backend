package com.example.wanted.controller;

import com.example.wanted.config.data.UserSession;
import com.example.wanted.request.PageInfo;
import com.example.wanted.request.PostModify;
import com.example.wanted.request.PostWrite;
import com.example.wanted.response.PostRead;
import com.example.wanted.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping
    public void posts(PageInfo pageInfo) {
        postService.posts(pageInfo);
    }

    @PostMapping("/write")
    public void write(UserSession userSession, PostWrite postWrite) {

        postService.write(userSession.getUserId(), postWrite);

    }

    @GetMapping("/{postId}")
    public PostRead read(@PathVariable Long postId) {

        return postService.read(postId);

    }

    @PatchMapping("/{postId}/modify")
    public void modify(UserSession userSession, @PathVariable Long postId, @RequestBody @Valid PostModify postModify) {

        postService.modify(userSession.getUserId(), postId, postModify);
    }

    @DeleteMapping("/{postId}/delete")
    public void delete(UserSession userSession, @PathVariable Long postId) {

        postService.delete(userSession.getUserId(), postId);

    }

}

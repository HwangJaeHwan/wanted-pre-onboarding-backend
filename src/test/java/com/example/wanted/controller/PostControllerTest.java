package com.example.wanted.controller;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.Post;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.PostModify;
import com.example.wanted.request.PostWrite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("/read")
    void 게시글_읽기() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        Post post = postRepository.save(new Post("test title", "test content", user));

        mockMvc.perform(get("/posts/{postId}", post.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("test title"))
                .andExpect(jsonPath("content").value("test content"))
                .andExpect(jsonPath("email").value("test@email.com"))
                .andDo(print());


    }


    @Test
    @DisplayName("/write")
    void 게시글_쓰기() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        PostWrite postWrite = new PostWrite("title", "content");

        String token = jwtProvider.getToken(user.getId());

        String json = objectMapper.writeValueAsString(postWrite);

        mockMvc.perform(post("/posts/write").header(HttpHeaders.AUTHORIZATION, token)
                .contentType(APPLICATION_JSON)
                .content(json));

    }

    @Test
    @DisplayName("/modify")
    void 게시글_수정() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        Post post = postRepository.save(new Post("before title", "before content", user));

        PostModify modify = new PostModify("after title", "after content");

        String token = jwtProvider.getToken(user.getId());

        String json = objectMapper.writeValueAsString(modify);

        mockMvc.perform(patch("/posts/{postId}/modify", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(print());


    }

    @Test
    @DisplayName("/delete")
    void 게시글_삭제() throws Exception{

        User user = userRepository.save(new User("test@email.com", "password1234"));

        Post post = postRepository.save(new Post("test title", "test content", user));

        String token = jwtProvider.getToken(user.getId());

        mockMvc.perform(delete("/posts/{postId}/delete", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andDo(print());


    }

    @Test
    @DisplayName("/posts")
    void 게시글_리스트() throws Exception{

        User user = userRepository.save(new User("test@email.com", "password1234"));

        List<Post> postList = new ArrayList<>();

        IntStream.rangeClosed(1, 100).forEach(
                i->{
                    postList.add(new Post("title" + i, "content" + i, user));
                }
        );
        postRepository.saveAll(postList);

        mockMvc.perform(get("/posts?page=2"))
                .andExpect(jsonPath("totalPage").value(10))
                .andExpect(jsonPath("infos.length()").value(10))
                .andExpect(jsonPath("isFirst").value(false))
                .andExpect(jsonPath("isLast").value(false))
                .andDo(print());

    }




}
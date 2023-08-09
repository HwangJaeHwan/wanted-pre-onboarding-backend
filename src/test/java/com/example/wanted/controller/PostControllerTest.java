package com.example.wanted.controller;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.Post;
import com.example.wanted.domain.User;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.PostWrite;
import com.example.wanted.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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



    @Test
    @DisplayName("/write")
    void 게시글_쓰기() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        PostWrite postWrite = new PostWrite("title", "content");

        String json = objectMapper.writeValueAsString(postWrite);
        mockMvc.perform(post("/post/write")
                .contentType(APPLICATION_JSON)
                .content(json))

    }

}
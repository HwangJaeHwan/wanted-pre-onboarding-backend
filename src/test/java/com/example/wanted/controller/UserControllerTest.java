package com.example.wanted.controller;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

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

    @Autowired
    PasswordEncoder encoder;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }




    @Test
    @DisplayName("/signup")
    void 회원가입() throws Exception {

        SignupRequest request = new SignupRequest("test@email.com", "password1234");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("/signup 이메일 형식 아님")
    void 회원가입_이메일형식_아님() throws Exception {

        SignupRequest request = new SignupRequest("test!email.com", "password1234");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
                .andExpect(jsonPath("$.validations.email").value("@를 입력해야합니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("/signup 비밀번호 8자리 이하")
    void 회원가입_비밀번호_8자리이하() throws Exception {

        SignupRequest request = new SignupRequest("test@email.com", "1234");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 입력입니다."))
                .andExpect(jsonPath("$.validations.password").value("비밂번호의 길이는 8자 이상입니다."))
                .andDo(print());

    }


    @Test
    @DisplayName("/login")
    void 로그인() throws Exception {

        userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        LoginRequest request = new LoginRequest("test@email.com", "password1234");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").isNotEmpty())
                .andExpect(jsonPath("$.refresh").isNotEmpty())
                .andDo(print());

    }

    @Test
    @DisplayName("/login 아이디 다름")
    void 로그인_아이디_다름() throws Exception {

        userRepository.save(new User("test11@email.com", encoder.encode("password1234")));

        LoginRequest request = new LoginRequest("test@email.com", "password1234");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디나 비밀번호가 다릅니다."))
                .andDo(print());

    }

    @Test
    @DisplayName("/login 비밀번호 다름")
    void 로그인_비밀번호_다름() throws Exception {

        userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        LoginRequest request = new LoginRequest("test@email.com", "password123456");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("아이디나 비밀번호가 다릅니다."))
                .andDo(print());

    }

}
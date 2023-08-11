package com.example.wanted.docs;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.wanted.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class UserControllerDocTest {

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
    void 회원가입() throws Exception {
        SignupRequest request = new SignupRequest("test@email.com", "password1234");
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/signup")
                        .content(json)
                        .contentType(APPLICATION_JSON))
                .andDo(document("signup",
                        requestFields(fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"))));
    }

    @Test
    void 로그인() throws Exception {


        userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        LoginRequest request = new LoginRequest("test@email.com", "password1234");

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(document("login",
                        requestFields(fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")),
                        responseFields(fieldWithPath("token").description("JWT 토근"))));

    }



}

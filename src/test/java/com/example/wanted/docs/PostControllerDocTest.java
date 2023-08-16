package com.example.wanted.docs;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.Post;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.PostModify;
import com.example.wanted.request.PostWrite;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.RestDocumentationExtension;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "api.wanted.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

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
    void 게시글_읽기() throws Exception {

        User user = userRepository.save(new User("test@email.com", "testPassword"));

        Post post = postRepository.save(new Post("test title", "test content", user));

        mockMvc.perform(get("/posts/{postId}", post.getId()))
                .andDo(document("post-read",
                        pathParameters(parameterWithName("postId").description("게시글 ID")),
                        responseFields(
                                fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"),
                                fieldWithPath("email").description("작성자 이메일"))));

    }

    @Test
    void 게시글_쓰기() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        PostWrite postWrite = new PostWrite("title", "content");

        String token = jwtProvider.getAccessToken(user.getId());

        String json = objectMapper.writeValueAsString(postWrite);

        mockMvc.perform(post("/posts/write").header(AUTHORIZATION, token)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(document("post-write",
                        requestHeaders(headerWithName(AUTHORIZATION).description("인증 토큰")),
                        requestFields(fieldWithPath("title").description("게시글 제목"),
                                fieldWithPath("content").description("게시글 내용"))));

    }

    @Test
    void 게시글_수정() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        Post post = postRepository.save(new Post("before title", "before content", user));

        PostModify modify = new PostModify("after title", "after content");

        String token = jwtProvider.getAccessToken(user.getId());

        String json = objectMapper.writeValueAsString(modify);

        mockMvc.perform(patch("/posts/{postId}/modify", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andDo(document("post-modify",
                        requestHeaders(headerWithName(AUTHORIZATION).description("인증 토큰")),
                        requestFields(fieldWithPath("title").description("수정할 제목"),
                                fieldWithPath("content").description("수정할 내용"))));
    }

    @Test
    void 게시글_삭제() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        Post post = postRepository.save(new Post("test title", "test content", user));

        String token = jwtProvider.getAccessToken(user.getId());

        mockMvc.perform(delete("/posts/{postId}/delete", post.getId())
                .header(HttpHeaders.AUTHORIZATION, token))
                .andDo(document("post-delete",
                        requestHeaders(headerWithName(AUTHORIZATION).description("인증 토큰")),
                        pathParameters(parameterWithName("postId").description("삭제할 게시글 ID"))));

    }

    @Test
    void 게시글_리스트() throws Exception {

        User user = userRepository.save(new User("test@email.com", "password1234"));

        List<Post> postList = new ArrayList<>();

        IntStream.rangeClosed(1, 100).forEach(
                i->{
                    postList.add(new Post("title" + i, "content" + i, user));
                }
        );
        postRepository.saveAll(postList);

        mockMvc.perform(get("/posts").queryParam("page", "2"))
                .andDo(document("post-list",
                        queryParameters(parameterWithName("page").description("현재 페이지")),
                        responseFields(
                                fieldWithPath("totalPage").description("전체 페이지 수"),
                                fieldWithPath("infos[].id").description("게시글 ID"),
                                fieldWithPath("infos[].title").description("게시글 제목"),
                                fieldWithPath("infos[].email").description("작성자 이메일"),
                                fieldWithPath("isFirst").description("첫 페이지 확인"),
                                fieldWithPath("isLast").description("마지막 페이지 확인")
                                )));
    }


}

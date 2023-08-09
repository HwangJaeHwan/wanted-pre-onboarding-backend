package com.example.wanted.service;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.Post;
import com.example.wanted.domain.User;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.PageInfo;
import com.example.wanted.request.PostModify;
import com.example.wanted.request.PostWrite;
import com.example.wanted.response.PostList;
import com.example.wanted.response.PostRead;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class PostServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder encoder;


    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("게시글 쓰기")
    void 게시글_쓰기() {

        User user = userRepository.save(new User("test@email.com", encoder.encode("password1234")));
        PostWrite postWrite = new PostWrite("title", "content");

        Long postId = postService.write(user.getId(), postWrite);
        Post find = postRepository.findById(postId).get();

        Assertions.assertEquals(1L, postRepository.count());
        Assertions.assertEquals("title", find.getTitle());
        Assertions.assertEquals("content", find.getContent());

    }

    @Test
    @DisplayName("게시글 읽기")
    void 게시글_읽기() {

        User user = userRepository.save(new User("test@email.com", encoder.encode("password1234")));
        Post post = new Post("title", "content", user);

        postRepository.save(post);

        PostRead read = postService.read(post.getId());

        assertEquals("title", read.getTitle());
        assertEquals("content", read.getContent());

    }

    @Test
    @DisplayName("게시글 수정")
    void 게시글_수정() {

        User user = userRepository.save(new User("test@email.com", encoder.encode("password1234")));
        Post post = new Post("title", "content", user);

        postRepository.save(post);

        PostModify modify = new PostModify("changeTitle", "changeContent");

        postService.modify(user.getId(), post.getId(), modify);

        Post changePost = postRepository.findById(post.getId()).get();

        assertEquals("changeTitle", changePost.getTitle());
        assertEquals("changeContent", changePost.getContent());



    }

    @Test
    @DisplayName("게시글 삭제")
    void 게시글_삭제() {

        User user = userRepository.save(new User("test@email.com", encoder.encode("password1234")));
        Post post = new Post("title", "content", user);

        postRepository.save(post);

        postService.delete(user.getId(), post.getId());

        assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("게시글 리스트")
    void 게시글_리스트() {

        User user = userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        List<Post> postList = new ArrayList<>();

        IntStream.rangeClosed(1, 100).forEach(
                i->{
                    postList.add(new Post("title" + i, "content" + i, user));
                }
        );
        postRepository.saveAll(postList);

        PageInfo pageInfo = new PageInfo();

        PostList posts = postService.posts(pageInfo);

        assertEquals(10, posts.getTotalPage());
        assertTrue(posts.getIsFirst());
        assertFalse(posts.getIsLast());
        assertEquals(10, posts.getInfos().size());

    }





}
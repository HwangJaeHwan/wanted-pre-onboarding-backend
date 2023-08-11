package com.example.wanted.service;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.User;
import com.example.wanted.exception.DuplicationEmailException;
import com.example.wanted.exception.LoginException;
import com.example.wanted.repository.PostRepository;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import com.example.wanted.response.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {


    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserService userService;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder encoder;


    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입")
    void 회원가입() {

        SignupRequest signupRequest = new SignupRequest("signup@email.com", "password1234");

        Long id = userService.signup(signupRequest);

        User user = userRepository.findById(id).get();

        assertEquals(1L, userRepository.count());
        assertEquals("signup@email.com",user.getEmail());
        assertTrue(encoder.matches("password1234", user.getPassword()));

    }

    @Test
    @DisplayName("회원가입 이메일 중복")
    void 이메일_중복() {

        userRepository.save(new User("same@email.com", "password1234"));

        SignupRequest signupRequest = new SignupRequest("same@email.com", "password1234");

        assertThrows(DuplicationEmailException.class, () -> userService.signup(signupRequest));

    }


    @Test
    @DisplayName("로그인")
    void 로그인() {

        User loginUser = userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        LoginRequest loginTest = new LoginRequest("test@email.com", "password1234");

        Token token = userService.login(loginTest);
        Long checkId = jwtProvider.parseToken(token.getToken());


        assertEquals(checkId, loginUser.getId());

    }

    @Test
    @DisplayName("로그인 실패")
    void 로그인실패() {

        userRepository.save(new User("test@email.com", encoder.encode("password1234")));

        LoginRequest loginTest = new LoginRequest("test@email.com", "diffpassword");

        assertThrows(LoginException.class, () -> userService.login(loginTest));

    }



}
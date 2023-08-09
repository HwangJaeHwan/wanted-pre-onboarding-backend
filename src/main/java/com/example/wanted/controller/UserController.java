package com.example.wanted.controller;

import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import com.example.wanted.response.Token;
import com.example.wanted.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid SignupRequest signupRequest) {

        userService.signup(signupRequest);

    }

    @PostMapping("/login")
    public Token login(@RequestBody @Valid LoginRequest loginRequest) {

        return userService.login(loginRequest);

    }
}

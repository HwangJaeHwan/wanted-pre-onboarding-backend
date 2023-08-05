package com.example.wanted.service;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.User;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;


    public void signup(SignupRequest signupRequest) {

        userRepository.save(new User(signupRequest.getEmail(), signupRequest.getPassword()));

    }


    public String login(LoginRequest loginRequest) {

        User user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(RuntimeException::new);

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException();
        }
        //jwt 토근 만들기
        return jwtProvider.getToken(user.getId());

    }



}

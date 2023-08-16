package com.example.wanted.service;

import com.example.wanted.config.jwt.JwtProvider;
import com.example.wanted.domain.User;
import com.example.wanted.exception.DuplicationEmailException;
import com.example.wanted.exception.LoginException;
import com.example.wanted.repository.UserRepository;
import com.example.wanted.request.LoginRequest;
import com.example.wanted.request.SignupRequest;
import com.example.wanted.response.Token;
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


    public Long signup(SignupRequest signupRequest) {

        if (userRepository.findUserByEmail(signupRequest.getEmail()).isPresent()) {
            throw new DuplicationEmailException();
        }

        User save = userRepository.save(new User(signupRequest.getEmail(), encoder.encode(signupRequest.getPassword())));

        return save.getId();

    }


    public Token login(LoginRequest loginRequest) {

        User user = userRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(LoginException::new);

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new LoginException();
        }

        return new Token(jwtProvider.getAccessToken(user.getId()), jwtProvider.getRefreshToken(user.getId()));

    }



}

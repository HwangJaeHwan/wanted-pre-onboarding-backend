package com.example.wanted.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {

    @Email(message = "email은 @을 입력해야합니다.")
    private String email;

    @Size(min = 8,message = "비밂번호의 길이는 8자 이상입니다.")
    private String password;


    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

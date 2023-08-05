package com.example.wanted.config.data;

import lombok.Getter;

@Getter
public class UserSession {

    private Long userId;

    public UserSession(Long userId) {
        this.userId = userId;
    }

}

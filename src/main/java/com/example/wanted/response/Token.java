package com.example.wanted.response;

import lombok.Getter;

@Getter
public class Token {

    private String access;
    private String refresh;

    public Token(String access,String refresh) {
        this.access = access;
        this.refresh = refresh;
    }
}

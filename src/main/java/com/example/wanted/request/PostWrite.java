package com.example.wanted.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class PostWrite {

    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;

    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;

    public PostWrite(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package com.example.wanted.exception;

public class PostNotFoundException extends MyException{

    private static final String MESSAGE = "해당 게시글을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
    public PostNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public String getStatusCode() {
        return "404";
    }
}

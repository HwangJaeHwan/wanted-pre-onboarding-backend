package com.example.wanted.exception;

public class MyJwtException extends MyException{

    private static final String MESSAGE = "토큰 오류.";

    public MyJwtException() {
        super(MESSAGE);
    }
    public MyJwtException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}

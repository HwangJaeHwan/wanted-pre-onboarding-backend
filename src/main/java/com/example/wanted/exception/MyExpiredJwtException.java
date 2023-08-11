package com.example.wanted.exception;

public class MyExpiredJwtException extends MyException{

    private static final String MESSAGE = "토큰 유효기간이 지났습니다.";

    public MyExpiredJwtException() {
        super(MESSAGE);
    }
    public MyExpiredJwtException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}

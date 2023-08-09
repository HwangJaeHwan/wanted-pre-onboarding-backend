package com.example.wanted.exception;

public class LoginException extends MyException{

    private static final String MESSAGE = "아이디나 비밀번호가 다릅니다.";

    public LoginException() {
        super(MESSAGE);
    }
    public LoginException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}

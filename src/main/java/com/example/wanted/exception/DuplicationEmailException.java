package com.example.wanted.exception;

public class DuplicationEmailException extends MyException{

    private static final String MESSAGE = "이메일이 중복됩니다.";

    public DuplicationEmailException() {
        super(MESSAGE);
    }
    public DuplicationEmailException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public String getStatusCode() {
        return "400";
    }
}

package com.example.newsservice.exception;

public class AlreadyExistsException extends CustomException {

    public  AlreadyExistsException(String message) {
        super(message);
    }

    public AlreadyExistsException(String messageKey, String str) {
        super(messageKey, str);
    }
}

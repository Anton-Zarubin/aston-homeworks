package com.example.newsservice.exception;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String messageKey, Long id) {
        super(messageKey, id);
    }

    public EntityNotFoundException(String messageKey, String str) {
        super(messageKey, str);
    }
}

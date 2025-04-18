package com.example.newsservice.exception;

import com.example.newsservice.util.ErrorMessageUtil;

public abstract class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String messageKey, Long id) {
        super(ErrorMessageUtil.getMessage(messageKey, id));
    }

    public CustomException(String messageKey, String str) {
        super(ErrorMessageUtil.getMessage(messageKey, str));
    }
}

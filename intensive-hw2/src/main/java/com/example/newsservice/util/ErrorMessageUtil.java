package com.example.newsservice.util;

import lombok.experimental.UtilityClass;

import java.text.MessageFormat;
import java.util.ResourceBundle;

@UtilityClass
public class ErrorMessageUtil {

    public String getMessage(String key) {
        return ResourceBundle.getBundle("error_message").getString(key);
    }

    public String getMessage(String key, Long id) {
        return MessageFormat.format(getMessage(key), id);
    }

    public String getMessage(String key, String str) {
        return MessageFormat.format(getMessage(key), str);
    }
}

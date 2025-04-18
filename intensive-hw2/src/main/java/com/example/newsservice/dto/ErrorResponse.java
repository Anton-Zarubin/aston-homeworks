package com.example.newsservice.dto;

import java.util.Date;

public record ErrorResponse(String message, Date timestamp) {
}

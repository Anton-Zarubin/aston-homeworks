package com.example.newsservice.util;

import com.example.newsservice.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class ExceptionHandler {

    public static void sqlExceptionHandler(HttpServletResponse response, ObjectMapper objectMapper, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        printResponse(response, objectMapper, message);
    }

    public static void badRequestExceptionHandler(HttpServletResponse response, ObjectMapper objectMapper, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        printResponse(response, objectMapper, message);
    }

    public static void notFoundExceptionHandler(HttpServletResponse response, ObjectMapper objectMapper, String message)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        printResponse(response, objectMapper, message);
    }

    private static void printResponse(HttpServletResponse response, ObjectMapper objectMapper, String message)
            throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(objectMapper.writeValueAsString(new ErrorResponse(message, new Date())));
        out.flush();
    }
}

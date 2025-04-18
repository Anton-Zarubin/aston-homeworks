package com.example.newsservice.servlet;

import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.DBException;
import com.example.newsservice.util.ExceptionHandler;
import com.example.newsservice.controller.NewsController;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/news/tags")
public class NewsTagsServlet extends HttpServlet {

    private final NewsController newsController = NewsController.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            newsController.addTag(Long.parseLong(req.getParameter("newsid")),
                    Long.parseLong(req.getParameter("tagid")));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Invalid parameters");
        } catch (AlreadyExistsException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, e.getMessage());
        } catch (DBException e) {
            ExceptionHandler.sqlExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            boolean isRemoved = newsController.removeTag(Long.parseLong(req.getParameter("newsid")),
                    Long.parseLong(req.getParameter("tagid")));
            if (!isRemoved) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, "Tag is not associated with the news");
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Invalid parameters");
        }
    }
}

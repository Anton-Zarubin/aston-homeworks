package com.example.newsservice.servlet;

import com.example.newsservice.exception.DBException;
import com.example.newsservice.util.ErrorMessageUtil;
import com.example.newsservice.util.ExceptionHandler;
import com.example.newsservice.controller.NewsController;
import com.example.newsservice.dto.news.NewsListResponse;
import com.example.newsservice.dto.news.NewsResponse;
import com.example.newsservice.dto.news.UpsertNewsRequest;
import com.example.newsservice.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/news/*")
public class NewsServlet extends HttpServlet {

    private final NewsController newsController = NewsController.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            NewsListResponse newsList = newsController.getAll();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), newsList);
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                NewsResponse news = newsController.getById(id);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), news);
            } catch (NumberFormatException e) {
                ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Invalid id");
            } catch (EntityNotFoundException e) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UpsertNewsRequest upsertNewsRequest = objectMapper.readValue(req.getInputStream(), UpsertNewsRequest.class);
            NewsResponse createdNews = newsController.createNews(upsertNewsRequest);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getOutputStream(), createdNews);
        } catch (EntityNotFoundException e) {
            ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"News id required");
            return;
        }
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            UpsertNewsRequest upsertNewsRequest = objectMapper.readValue(req.getInputStream(), UpsertNewsRequest.class);
            boolean isUpdated = newsController.updateNews(id, upsertNewsRequest);
            if (!isUpdated) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("newsById", id));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Invalid id");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"News id required");
            return;
        }
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean isDeleted = newsController.deleteNews(id);
            if (!isDeleted) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("newsById", id));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Invalid id");
        } catch (DBException e) {
            ExceptionHandler.sqlExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }
}

package com.example.newsservice.servlet;

import com.example.newsservice.exception.DBException;
import com.example.newsservice.util.ErrorMessageUtil;
import com.example.newsservice.util.ExceptionHandler;
import com.example.newsservice.controller.TagController;
import com.example.newsservice.dto.tag.TagListResponse;
import com.example.newsservice.dto.tag.TagResponse;
import com.example.newsservice.dto.tag.UpsertTagRequest;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/tags/*")
public class TagServlet extends HttpServlet {

    private final TagController tagController = TagController.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            TagListResponse tags = tagController.getAll();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), tags);
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                TagResponse tag = tagController.getById(id);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), tag);
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
            UpsertTagRequest upsertTagRequest = objectMapper.readValue(req.getInputStream(), UpsertTagRequest.class);
            TagResponse createdTag = tagController.createTag(upsertTagRequest);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getOutputStream(), createdTag);
        } catch (AlreadyExistsException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Tag id required");
            return;
        }
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            UpsertTagRequest upsertTagRequest = objectMapper.readValue(req.getInputStream(), UpsertTagRequest.class);
            boolean isUpdated = tagController.updateTag(id, upsertTagRequest);
            if (!isUpdated) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("tagById", id));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Invalid id");
        } catch (AlreadyExistsException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper,"Tag id required");
            return;
        }
        try {
            Long id = Long.parseLong(req.getPathInfo().substring(1));
            boolean isDeleted = tagController.deleteTag(id);
            if (!isDeleted) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("tagById", id));
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

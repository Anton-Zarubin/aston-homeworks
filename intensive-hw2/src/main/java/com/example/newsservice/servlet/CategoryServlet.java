package com.example.newsservice.servlet;

import com.example.newsservice.controller.CategoryController;
import com.example.newsservice.util.ErrorMessageUtil;
import com.example.newsservice.util.ExceptionHandler;
import com.example.newsservice.dto.category.CategoryListResponse;
import com.example.newsservice.dto.category.CategoryResponse;
import com.example.newsservice.dto.category.UpsertCategoryRequest;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/categories/*")
public class CategoryServlet extends HttpServlet {

    private final CategoryController categoryController = CategoryController.getInstance();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            CategoryListResponse categories = categoryController.getAll();
            resp.setContentType("application/json");
            objectMapper.writeValue(resp.getOutputStream(), categories);
        } else {
            try {
                Long id = Long.parseLong(pathInfo.substring(1));
                CategoryResponse category = categoryController.getById(id);
                resp.setContentType("application/json");
                objectMapper.writeValue(resp.getOutputStream(), category);
            } catch (NumberFormatException e) {
                ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Invalid id");
            } catch (EntityNotFoundException e) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, e.getMessage());
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            UpsertCategoryRequest upsertCategoryRequest = objectMapper.readValue(req.getInputStream(), UpsertCategoryRequest.class);
            CategoryResponse createdCategory = categoryController.createCategory(upsertCategoryRequest);
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getOutputStream(), createdCategory);
        } catch (AlreadyExistsException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Category id required");
            return;
        }
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            UpsertCategoryRequest upsertCategoryRequest = objectMapper.readValue(req.getInputStream(), UpsertCategoryRequest.class);
            boolean isUpdated = categoryController.updateCategory(id, upsertCategoryRequest);
            if (!isUpdated) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("categoryById", id));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Invalid id");
        } catch (AlreadyExistsException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Category id required");
            return;
        }
        try {
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean isDeleted = categoryController.deleteCategory(id);
            if (isDeleted) {
                ExceptionHandler.notFoundExceptionHandler(resp, objectMapper, ErrorMessageUtil.getMessage("categoryById", id));
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NumberFormatException e) {
            ExceptionHandler.badRequestExceptionHandler(resp, objectMapper, "Invalid id");
        }
    }
}

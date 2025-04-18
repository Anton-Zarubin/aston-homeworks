package com.example.newsservice.repository.impl;

import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.DBException;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CategoryRepository implements CrudRepository<Long, Category> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM categories c LEFT JOIN news n ON c.category_id = n.category_id";

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE c.category_id = ?";

    private static final String FIND_BY_TITLE_QUERY = FIND_ALL_QUERY + " WHERE c.category_title = ?";

    private static final String SAVE_QUERY = "INSERT INTO categories (category_title) VALUES (?)";

    private static final String UPDATE_QUERY = "UPDATE categories SET category_title = ? WHERE category_id = ?";

    private static final String DELETE_QUERY = "DELETE FROM categories WHERE category_id = ?";

    private static final String EXIST_BY_ID_QUERY = "SELECT exists (SELECT 1 FROM categories WHERE category_id = ? LIMIT 1)";

    private final String UNIQUE_VIOLATION = "23505";

    private static CrudRepository<Long, Category> instance;

    private CategoryRepository() {
    }

    public static synchronized CrudRepository<Long, Category> getInstance() {
        if (instance == null) {
            instance = new CategoryRepository();
        }
        return instance;
    }

    @Override
    public List<Category> findAll() {
        Map<Category, List<News>> newsByCategory = new LinkedHashMap<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet rs = statement.executeQuery();
            Category currentCategory = null;
            long id = 0L;
            while (rs.next()) {
                if (rs.getLong("category_id") != id) {
                    id = rs.getLong("category_id");
                    currentCategory = new Category();
                    currentCategory.setId(id);
                    currentCategory.setTitle(rs.getString("category_title"));
                    newsByCategory.put(currentCategory, new ArrayList<>());
                }
                newsByCategory.get(currentCategory).add(resultSetToNews(rs));
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }

        return newsByCategory.entrySet().stream()
                .peek(entry -> entry.getKey().setNews(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Category> findById(Long id) {
        Category category = new Category();
        List<News> newsList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (!rs.next()) {
                return Optional.empty();
            } else {
                category.setId(id);
                category.setTitle(rs.getString("category_title"));
                newsList.add(resultSetToNews(rs));
            }
            while (rs.next()) {
                newsList.add(resultSetToNews(rs));
            }
            category.setNews(newsList);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return Optional.of(category);
    }

    @Override
    public Optional<Category> findByName(String title) {
        Category category = new Category();
        List<News> newsList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_TITLE_QUERY)) {
            statement.setString(1, title);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            } else {
                category.setId(rs.getLong("category_id"));
                category.setTitle(title);
                newsList.add(resultSetToNews(rs));
            }
            while (rs.next()) {
                newsList.add(resultSetToNews(rs));
            }
            category.setNews(newsList);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return Optional.of(category);
    }

    @Override
    public Category save(Category category) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, category.getTitle());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                category.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new AlreadyExistsException("duplicateCategory", category.getTitle());
            } else {
                throw new DBException(e.getMessage());
            }
        }
        return category;
    }

    @Override
    public boolean update(Long id, Category category) {
        boolean isUpdated;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, category.getTitle());
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new AlreadyExistsException("duplicateCategory", category.getTitle());
            } else {
                throw new DBException(e.getMessage());
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean isDeleted;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setLong(1, id);
            isDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return isDeleted;
    }

    @Override
    public boolean existsById(Long id) {
        boolean isExists = false;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXIST_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                isExists = rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return isExists;
    }

    private News resultSetToNews(ResultSet rs) throws SQLException {
        News news = new News();
        news.setId(rs.getLong("news_id"));
        news.setTitle(rs.getString("news_title"));
        news.setText(rs.getString("text"));
        news.setCreatedAt(rs.getDate("created_at"));
        return news;
    }
}

package com.example.newsservice.repository.impl;

import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.entity.Tag;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.DBException;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.util.DBConnection;

import java.sql.*;

import java.util.*;
import java.util.stream.Collectors;

public class NewsRepositoryImpl implements NewsRepository {

    private static final String FIND_ALL_QUERY = """ 
    SELECT * FROM news n JOIN categories c ON c.category_id = n.category_id
    LEFT JOIN news_tags ON n.news_id = news_id_nt LEFT JOIN tags t ON tag_id_nt = t.tag_id
    """;

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE n.news_id = ?";

    private static final String SAVE_QUERY = "INSERT INTO news (news_title, text, category_id) VALUES (?, ?, ?)";

    private static final String ADD_TAG_TO_NEWS_QUERY = "INSERT INTO news_tags (news_id_nt, tag_id_nt) VALUES (?, ?)";

    private static final String REMOVE_TAG_FROM_NEWS_QUERY = "DELETE FROM news_tags WHERE news_id_nt = ? AND tag_id_nt = ?";

    private static final String UPDATE_QUERY = "UPDATE news SET news_title = ?, text = ?, category_id = ? WHERE news_id = ?";

    private static final String DELETE_QUERY = "DELETE FROM news WHERE news_id = ?";

    private static final String EXIST_BY_ID_QUERY = "SELECT exists (SELECT 1 FROM news WHERE news_id = ? LIMIT 1)";

    private final String UNIQUE_VIOLATION = "23505";

    private static NewsRepository instance;

    private NewsRepositoryImpl() {
    }

    public static synchronized NewsRepository getInstance() {
        if (instance == null) {
            instance = new NewsRepositoryImpl();
        }
        return instance;
    }

    @Override
    public List<News> findAll() {
        Map<News, List<Tag>> tagsByNews = new LinkedHashMap<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet rs = statement.executeQuery();
            News currentNews = null;
            long id = 0L;
            while (rs.next()) {
                if (rs.getLong("news_id") != id) {
                    id = rs.getLong("news_id");
                    currentNews = resultSetToNews(rs);
                    currentNews.setCategory(resultSetToCategory(rs));
                    tagsByNews.put(currentNews, new ArrayList<>());
                }
                tagsByNews.get(currentNews).add(resultSetToTag(rs));
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }

        return tagsByNews.entrySet().stream()
                .peek(entry -> entry.getKey().setTags(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<News> findById(Long id) {
        News news;
        List<Tag> tags = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            } else {
                news = resultSetToNews(rs);
                news.setCategory(resultSetToCategory(rs));
                tags.add(resultSetToTag(rs));
            }
            while (rs.next()) {
                tags.add(resultSetToTag(rs));
            }
            news.setTags(tags);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return Optional.of(news);
    }

    @Override
    public Optional<News> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public News save(News news) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getText());
            statement.setLong(3, news.getCategory().getId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                news.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return news;
    }

    @Override
    public boolean update(Long id, News news) {
        boolean isUpdated;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, news.getTitle());
            statement.setString(2, news.getText());
            statement.setLong(3, news.getCategory().getId());
            statement.setLong(4, id);
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return isUpdated;
    }

    @Override
    public void addTag(Long newsId, Long tagId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADD_TAG_TO_NEWS_QUERY)) {
            statement.setLong(1, newsId);
            statement.setLong(2, tagId);
            statement.executeUpdate();
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new AlreadyExistsException("The tag is already associated with the news");
            } else {
                throw new DBException(e.getMessage());
            }
        }
    }

    @Override
    public boolean removeTag(Long newsId, Long tagId) {
        boolean isRemoved;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(REMOVE_TAG_FROM_NEWS_QUERY)) {
            statement.setLong(1, newsId);
            statement.setLong(2, tagId);
            isRemoved = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return isRemoved;
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

    private Category resultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getLong("category_id"));
        category.setTitle(rs.getString("category_title"));
        return category;
    }

    private Tag resultSetToTag(ResultSet rs) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getLong("tag_id"));
        tag.setName(rs.getString("name"));
        return tag;
    }
}

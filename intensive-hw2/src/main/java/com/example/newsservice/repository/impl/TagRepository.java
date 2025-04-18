package com.example.newsservice.repository.impl;

import com.example.newsservice.entity.News;
import com.example.newsservice.entity.Tag;
import com.example.newsservice.exception.AlreadyExistsException;
import com.example.newsservice.exception.DBException;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.util.DBConnection;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class TagRepository implements CrudRepository<Long, Tag> {

    private static final String FIND_ALL_QUERY = """
    SELECT * FROM tags t LEFT JOIN news_tags ON t.tag_id = tag_id_nt LEFT JOIN news n ON news_id_nt = n.news_id
    """;

    private static final String FIND_BY_ID_QUERY = FIND_ALL_QUERY + " WHERE t.tag_id = ?";

    private static final String SAVE_QUERY = "INSERT INTO tags (name) VALUES (?)";

    private static final String UPDATE_QUERY = "UPDATE tags SET name = ? WHERE tag_id = ?";

    private static final String DELETE_QUERY = "DELETE FROM tags WHERE tag_id = ?";

    private static final String EXIST_BY_ID_QUERY = "SELECT exists (SELECT 1 FROM tags WHERE tag_id = ? LIMIT 1)";

    private final String UNIQUE_VIOLATION = "23505";

    private static CrudRepository<Long, Tag> instance;

    private TagRepository() {
    }

    public static synchronized CrudRepository<Long, Tag> getInstance() {
        if (instance == null) {
            instance = new TagRepository();
        }
        return instance;
    }

    @Override
    public List<Tag> findAll() {
        Map<Tag, List<News>> newsByTag = new LinkedHashMap<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {
            ResultSet rs = statement.executeQuery();
            Tag currentTag = null;
            long id = 0L;
            while (rs.next()) {
                if (rs.getLong("tag_id") != id) {
                    id = rs.getLong("tag_id");
                    currentTag = new Tag();
                    currentTag.setId(id);
                    currentTag.setName(rs.getString("name"));
                    newsByTag.put(currentTag, new ArrayList<>());
                }
                newsByTag.get(currentTag).add(resultSetToNews(rs));
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }

        return newsByTag.entrySet().stream()
                .peek(entry -> entry.getKey().setNews(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Tag> findById(Long id) {
        Tag tag = new Tag();
        List<News> newsList = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            } else {
                tag.setId(id);
                tag.setName(rs.getString("name"));
                newsList.add(resultSetToNews(rs));
            }
            while (rs.next()) {
                newsList.add(resultSetToNews(rs));
            }
            tag.setNews(newsList);
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
        return Optional.of(tag);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public Tag save(Tag tag) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tag.getName());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                tag.setId(generatedKeys.getLong(1));
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new AlreadyExistsException("duplicateTag", tag.getName());
            } else {
                throw new DBException(e.getMessage());
            }
        }
        return tag;
    }

    @Override
    public boolean update(Long id, Tag tag) {
        boolean isUpdated;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, tag.getName());
            statement.setLong(2, id);
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new AlreadyExistsException("duplicateTag", tag.getName());
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

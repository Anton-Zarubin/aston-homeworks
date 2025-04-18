package com.example.repository;

import com.example.newsservice.entity.Category;
import com.example.newsservice.entity.News;
import com.example.newsservice.repository.NewsRepository;
import com.example.newsservice.repository.impl.CategoryRepository;
import com.example.newsservice.repository.impl.NewsRepositoryImpl;
import com.example.newsservice.repository.CrudRepository;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NewsRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test_db")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(5432), new ExposedPort(5432)))
            ))
            .withInitScript("init.sql");

    public static NewsRepository newsRepository;

    public static CrudRepository<Long, Category> categoryRepository;

    private static long id;

    private  static Category category;

    @BeforeAll
    static void beforeAll() {
        container.start();
        newsRepository = NewsRepositoryImpl.getInstance();
        categoryRepository = CategoryRepository.getInstance();
        category = new Category();
        category.setTitle("some category");
        category = categoryRepository.save(category);
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    @Order(1)
    void save() {
        String expectedTitle = "new news";
        News news = new News();
        news.setTitle(expectedTitle);
        news.setText("some text");
        news.setCategory(category);
        news = newsRepository.save(news);
        id = news.getId();

        Optional<News> resultNews = newsRepository.findById(id);

        Assertions.assertTrue(resultNews.isPresent());
        Assertions.assertEquals(expectedTitle, resultNews.get().getTitle());
    }

    @Test
    @Order(2)
    void update() {
        String expectedTitle = "changed news";
        String expectedText = "any text";
        News changedNews = new News();
        changedNews.setTitle(expectedTitle);
        changedNews.setText(expectedText);
        changedNews.setCategory(category);
        newsRepository.update(id, changedNews);

        Optional<News> resultNews = newsRepository.findById(id);

        Assertions.assertEquals(expectedTitle, resultNews.get().getTitle());
        Assertions.assertEquals(expectedText, resultNews.get().getText());
    }

    @Test
    @Order(3)
    void deleteById() {
        newsRepository.deleteById(id);

        Assertions.assertEquals(Optional.empty(), newsRepository.findById(id));
    }
}

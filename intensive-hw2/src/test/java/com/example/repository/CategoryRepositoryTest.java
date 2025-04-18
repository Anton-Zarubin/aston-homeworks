package com.example.repository;

import com.example.newsservice.entity.Category;
import com.example.newsservice.repository.impl.CategoryRepository;
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
public class CategoryRepositoryTest {

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

    public static CrudRepository<Long, Category> repository;

    private static long id;

    @BeforeAll
    static void beforeAll() {
        container.start();
        repository = CategoryRepository.getInstance();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    @Order(1)
    void save() {
        String expectedTitle = "new category";
        Category category = new Category();
        category.setTitle(expectedTitle);
        category = repository.save(category);
        id = category.getId();

        Optional<Category> resultCategory = repository.findById(id);

        Assertions.assertTrue(resultCategory.isPresent());
        Assertions.assertEquals(expectedTitle, resultCategory.get().getTitle());
    }

    @Test
    @Order(2)
    void update() {
        String expectedTitle = "changed category";
        Category changedCategory = new Category();
        changedCategory.setTitle(expectedTitle);
        repository.update(id, changedCategory);

        Optional<Category> resultCategory = repository.findById(id);

        Assertions.assertEquals(expectedTitle, resultCategory.get().getTitle());
    }

    @Test
    @Order(3)
    void deleteById() {
        repository.deleteById(id);

        Assertions.assertEquals(Optional.empty(), repository.findById(id));
    }
}

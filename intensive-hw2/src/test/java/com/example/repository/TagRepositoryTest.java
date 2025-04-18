package com.example.repository;

import com.example.newsservice.entity.Tag;
import com.example.newsservice.repository.CrudRepository;
import com.example.newsservice.repository.impl.TagRepository;
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
public class TagRepositoryTest {

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

    public static CrudRepository<Long, Tag> repository;

    private static long id;

    @BeforeAll
    static void beforeAll() {
        container.start();
        repository = TagRepository.getInstance();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    @Order(1)
    void save() {
        String expectedName = "new tag";
        Tag tag = new Tag();
        tag.setName(expectedName);
        tag = repository.save(tag);
        id = tag.getId();

        Optional<Tag> resultTag = repository.findById(id);

        Assertions.assertTrue(resultTag.isPresent());
        Assertions.assertEquals(expectedName, resultTag.get().getName());
    }

    @Test
    @Order(2)
    void update() {
        String expectedName = "changed tag";
        Tag changedTag = new Tag();
        changedTag.setName(expectedName);
        repository.update(id, changedTag);

        Optional<Tag> resultTag = repository.findById(id);

        Assertions.assertEquals(expectedName, resultTag.get().getName());
    }

    @Test
    @Order(3)
    void deleteById() {
        repository.deleteById(id);

        Assertions.assertEquals(Optional.empty(), repository.findById(id));
    }
}
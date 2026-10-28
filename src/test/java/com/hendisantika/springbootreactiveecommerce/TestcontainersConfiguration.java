package com.hendisantika.springbootreactiveecommerce;

import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Shared Testcontainers for MongoDB and RabbitMQ, wired into Spring Boot via {@link ServiceConnection}.
 * <p>
 * The containers are started eagerly (once per JVM) so they are always running before Spring resolves
 * the connection details, avoiding a "container should be started first" race during context startup.
 * <p>
 * Image versions live in {@code src/test/resources/testcontainers/*.Dockerfile} so Dependabot can update them.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    // mongod output is streamed into the test log so container crashes are visible on CI
    static final MongoDBContainer MONGO_DB = new MongoDBContainer(imageFrom("mongo.Dockerfile"))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("container.mongo")));

    static final RabbitMQContainer RABBIT_MQ = new RabbitMQContainer(imageFrom("rabbitmq.Dockerfile"));

    static {
        Startables.deepStart(MONGO_DB, RABBIT_MQ).join();
    }

    @Bean
    @ServiceConnection
    MongoDBContainer mongoDbContainer() {
        return MONGO_DB;
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitContainer() {
        return RABBIT_MQ;
    }

    /**
     * Reads the image from the {@code FROM} line of a Dockerfile in {@code testcontainers/} on the test classpath.
     */
    static DockerImageName imageFrom(String dockerfile) {
        String resource = "/testcontainers/" + dockerfile;
        try (InputStream in = TestcontainersConfiguration.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("Missing test resource " + resource);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8).lines()
                    .map(String::strip)
                    .filter(line -> line.regionMatches(true, 0, "FROM ", 0, 5))
                    .map(line -> DockerImageName.parse(line.substring(5).strip()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No FROM line in " + resource));
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + resource, e);
        }
    }
}

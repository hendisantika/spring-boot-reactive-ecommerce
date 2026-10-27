package com.hendisantika.springbootreactiveecommerce;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.mongodb.MongoDBContainer;
import org.testcontainers.rabbitmq.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

/**
 * Shared Testcontainers for MongoDB and RabbitMQ, wired into Spring Boot via {@link ServiceConnection}.
 * <p>
 * The containers are started eagerly (once per JVM) so they are always running before Spring resolves
 * the connection details, avoiding a "container should be started first" race during context startup.
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    static final MongoDBContainer MONGO_DB = new MongoDBContainer(DockerImageName.parse("mongo:8.0"));

    static final RabbitMQContainer RABBIT_MQ = new RabbitMQContainer(DockerImageName.parse("rabbitmq:4-management-alpine"));

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
}

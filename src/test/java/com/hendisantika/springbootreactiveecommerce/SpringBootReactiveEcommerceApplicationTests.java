package com.hendisantika.springbootreactiveecommerce;

import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(properties = {"spring.mongodb.embedded.version=5.0.15"})
@AutoConfigureWebTestClient
@Testcontainers
class SpringBootReactiveEcommerceApplicationTests {


    @Container
    static RabbitMQContainer rabbitContainer = new RabbitMQContainer("rabbitmq:3.7.25-management-alpine");

    @Autowired
    WebTestClient client;

    @Autowired
    ItemRepository itemRepository;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitContainer::getAmqpPort);
    }

    @BeforeEach
    void setUp() {
        itemRepository.findAll()
                .flatMap(itemRepository::delete)
                .blockLast();
    }

}

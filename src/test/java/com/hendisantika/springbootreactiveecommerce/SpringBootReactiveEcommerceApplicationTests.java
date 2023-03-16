package com.hendisantika.springbootreactiveecommerce;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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

    @Test
    @WithMockUser(username = "admin", roles = {"INVENTORY"})
    void itemsAreAdded_viaAmqp() {
        client.post()
                .uri("/amqp/items/add")
                .bodyValue(new Item("Desktop", 1999.98))
                .exchange()
                .expectStatus().isCreated()
                .expectBody();

        itemRepository.findAll()
                .as(StepVerifier::create)
                .assertNext(item -> {
                    assertThat(item.getPrice(), equalTo(1999.98));
                    assertThat(item.getName(), equalTo("Desktop"));
                })
                .verifyComplete();
    }

}

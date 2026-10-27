package com.hendisantika.springbootreactiveecommerce;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.time.Duration;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:48
 * To change this template use File | Settings | File Templates.
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureWebTestClient
public class SecurityIT {

    @Autowired
    WebTestClient client;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @WithMockUser(username = "jay")
    void addingItem_withoutProperRole_returnsForbidden() {
        client.post()
                .uri("/amqp/items/add")
                .bodyValue(new Item("iPhone", 999.87))
                .exchange()
                .expectStatus()
                .isForbidden();
    }

    @Test
    @WithMockUser(username = "admin", roles = "INVENTORY")
    void addingItem_withProperRole_returnsCreated() {
        Item item = new Item("iPhone", 999.87);
        client.post()
                .uri("/amqp/items/add")
                .bodyValue(item)
                .exchange()
                .expectStatus()
                .isCreated();

        // the item is persisted asynchronously by the RabbitMQ listener
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> itemRepository.findAll(Example.of(item))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete());
    }
}

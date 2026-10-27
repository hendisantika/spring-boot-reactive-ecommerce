package com.hendisantika.springbootreactiveecommerce;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.awaitility.Awaitility;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureWebTestClient
class SpringBootReactiveEcommerceApplicationTests {

    @Autowired
    WebTestClient client;

    @Autowired
    ItemRepository itemRepository;

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

        // the item is persisted asynchronously by the RabbitMQ listener
        Awaitility.await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> itemRepository.findAll()
                .as(StepVerifier::create)
                .assertNext(item -> {
                    assertThat(item.getPrice(), equalTo(1999.98));
                    assertThat(item.getName(), equalTo("Desktop"));
                })
                .verifyComplete());
    }

}

package com.hendisantika.springbootreactiveecommerce;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

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
@SpringBootTest(properties = {"spring.mongodb.embedded.version=5.0.15"})
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
}

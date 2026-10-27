package com.hendisantika.springbootreactiveecommerce.repository;

import com.hendisantika.springbootreactiveecommerce.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:49
 * To change this template use File | Settings | File Templates.
 */
@DataMongoTest
@Import(TestcontainersConfiguration.class)
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        // the MongoDB container is shared across test classes, so start from an empty collection
        itemRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        itemRepository.findAll()
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();
    }

}

package com.hendisantika.springbootreactiveecommerce.service;

import com.hendisantika.springbootreactiveecommerce.repository.CartRepository;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:50
 * To change this template use File | Settings | File Templates.
 */
class CartServiceTest {
    ItemRepository mockItemRepository;
    CartRepository mockCartRepository;
    CartService cartService;

    @BeforeEach
    void setup() {
        mockItemRepository = mock(ItemRepository.class);
        mockCartRepository = mock(CartRepository.class);
        cartService = new CartService(mockItemRepository, mockCartRepository);
    }

}

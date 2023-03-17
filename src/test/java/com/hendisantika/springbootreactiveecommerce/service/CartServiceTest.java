package com.hendisantika.springbootreactiveecommerce.service;

import com.hendisantika.springbootreactiveecommerce.entity.Cart;
import com.hendisantika.springbootreactiveecommerce.entity.CartItem;
import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.CartRepository;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void getItems_returnsItems() {
        when(mockItemRepository.findAll())
                .thenReturn(Flux.just(new Item("alarm clock", 19.99)));

        cartService.getItems()
                .as(StepVerifier::create)
                .assertNext(item -> {
                    assertThat(item.getName(), equalTo("alarm clock"));
                    assertThat(item.getPrice(), equalTo(19.99));
                })
                .verifyComplete();
    }

    @Test
    void getItems_callsRepository() {
        when(mockItemRepository.findAll())
                .thenReturn(Flux.empty());

        StepVerifier.create(cartService.getItems())
                .verifyComplete();

        verify(mockItemRepository).findAll();
    }

    @Test
    void addItemToCart_onEmptyCart_returnsOneCartItem() {
        Item alarmClock = new Item("alarm clock", 19.99);
        when(mockItemRepository.findById("item id"))
                .thenReturn(Mono.just(alarmClock));

        when(mockCartRepository.findById("My cart"))
                .thenReturn(Mono.empty());

        Cart sampleCart = new Cart("My cart");
        sampleCart.setCartItems(List.of(new CartItem(alarmClock)));
        when(mockCartRepository.save(any(Cart.class)))
                .thenReturn(Mono.just(sampleCart));

        cartService.addItemToCart("My cart", "item id")
                .as(StepVerifier::create)
                .assertNext(cart -> {
                    List<CartItem> cartItems = cart.getCartItems();
                    assertThat(cartItems.size(), equalTo(1));
                    assertThat(cartItems.get(0).getQuantity(), equalTo(1));
                    Item item = cartItems.get(0).getItem();
                    assertThat(item.getName(), equalTo("alarm clock"));
                    assertThat(item.getPrice(), equalTo(19.99));
                })
                .verifyComplete();
    }
}

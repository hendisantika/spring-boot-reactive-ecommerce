package com.hendisantika.springbootreactiveecommerce.service;

import com.hendisantika.springbootreactiveecommerce.entity.Cart;
import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.CartRepository;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/16/23
 * Time: 06:42
 * To change this template use File | Settings | File Templates.
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Flux<Item> getItems() {
        return itemRepository.findAll();
    }

    public Mono<Cart> getCart(String cartName) {
        return cartRepository.findById(cartName)
                .defaultIfEmpty(new Cart(cartName));
    }
}

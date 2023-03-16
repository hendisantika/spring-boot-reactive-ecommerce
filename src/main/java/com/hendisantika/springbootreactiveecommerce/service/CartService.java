package com.hendisantika.springbootreactiveecommerce.service;

import com.hendisantika.springbootreactiveecommerce.entity.Cart;
import com.hendisantika.springbootreactiveecommerce.entity.CartItem;
import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.CartRepository;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

    public Mono<Cart> addItemToCart(String cartName, String itemId) {
        return cartRepository.findById(cartName)
                .defaultIfEmpty(new Cart(cartName))
                .flatMap(cart -> {
                    return cart.getCartItems().stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                            .findAny()
                            .map(cartItem -> {
                                cartItem.increment();
                                return Mono.just(cart);
                            })
                            .orElseGet(() -> {
                                return this.itemRepository.findById(itemId)
                                        .map(CartItem::new)
                                        .map(cartItem -> {
                                            cart.getCartItems().add(cartItem);
                                            return cart;
                                        });
                            });
                })
                .flatMap(cartRepository::save);
    }

    public Flux<Item> searchItems(String name, double price, boolean useAnd) {
        Item item = new Item(name, price);

        ExampleMatcher matcher = useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny();
        matcher = matcher
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withMatcher("price", ExampleMatcher.GenericPropertyMatchers.exact());
        Example<Item> probe = Example.of(item, matcher);

        return itemRepository.findAll(probe);
    }
}

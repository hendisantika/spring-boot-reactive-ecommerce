package com.hendisantika.springbootreactiveecommerce.controller;

import com.hendisantika.springbootreactiveecommerce.entity.Cart;
import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:25
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final CartService cartService;

    @GetMapping("/items")
    Flux<Item> items() {
        return cartService.getItems();
    }

    @GetMapping("/items/search")
    Flux<Item> search(String name, Double price, boolean useAnd) {
        return cartService.searchItems(name, price == null ? 0.0 : price, useAnd);
    }

    @GetMapping("/cart")
    Mono<Cart> cart(@AuthenticationPrincipal UserDetails userDetails) {
        return cartService.getCart(cartName(userDetails));
    }

    @PostMapping("/cart/add/{id}")
    Mono<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
        return cartService.addItemToCart(cartName(userDetails), id);
    }
}

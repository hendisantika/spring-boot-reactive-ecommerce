package com.hendisantika.springbootreactiveecommerce.controller;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
}

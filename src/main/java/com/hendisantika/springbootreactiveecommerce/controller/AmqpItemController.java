package com.hendisantika.springbootreactiveecommerce.controller;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

import static com.hendisantika.springbootreactiveecommerce.constans.MessageConstants.EXCHANGE;
import static com.hendisantika.springbootreactiveecommerce.constans.MessageConstants.ROUTING_KEY;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:24
 * To change this template use File | Settings | File Templates.
 */
@RestController
public class AmqpItemController {

    private final AmqpTemplate template;

    public AmqpItemController(AmqpTemplate template) {
        this.template = template;
    }

    @PreAuthorize("hasRole('INVENTORY')")
    @PostMapping("/amqp/items/add")
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.subscribeOn(Schedulers.boundedElastic())
                .flatMap(content -> {
                    return Mono.fromCallable(() -> {
                        template.convertAndSend(EXCHANGE, ROUTING_KEY, content);
                        return ResponseEntity.created(URI.create("/items"))
                                .build();
                    });
                });
    }
}

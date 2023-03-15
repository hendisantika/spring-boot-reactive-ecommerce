package com.hendisantika.springbootreactiveecommerce.service;

import com.hendisantika.springbootreactiveecommerce.constans.MessageConstants;
import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
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
public class AmqpItemService {

    private final ItemRepository itemRepository;

    @RabbitListener(
            ackMode = "MANUAL",
            queues = MessageConstants.QUEUE
    )
    public Mono<Void> processNewItems(Item item) {
        return this.itemRepository.save(item)
                .log()
                .then();
    }
}

package com.hendisantika.springbootreactiveecommerce.loader;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import com.hendisantika.springbootreactiveecommerce.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/16/23
 * Time: 06:40
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ItemLoader {

    @Bean
    CommandLineRunner initializeItems(ItemRepository repository) {
        return args -> {
            repository.findAll()
                    .flatMap(repository::delete)
                    .blockLast();

            List<Item> items = List.of(
                    new Item("alarm clock", 19.99),
                    new Item("Smart TV", 249.99)
            );
            Flux.fromIterable(items)
                    .flatMap(repository::save)
                    .blockLast();
        };
    }
}

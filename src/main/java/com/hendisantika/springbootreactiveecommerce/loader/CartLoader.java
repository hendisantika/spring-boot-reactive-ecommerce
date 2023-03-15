package com.hendisantika.springbootreactiveecommerce.loader;

import com.hendisantika.springbootreactiveecommerce.repository.CartRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

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
public class CartLoader {

    @Bean
    CommandLineRunner resetCart(CartRepository repository) {
        return args -> {
            repository.findAll()
                    .flatMap(repository::delete)
                    .blockLast();
        };
    }
}

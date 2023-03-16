package com.hendisantika.springbootreactiveecommerce.loader;

import com.hendisantika.springbootreactiveecommerce.entity.User;
import com.hendisantika.springbootreactiveecommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:21
 * To change this template use File | Settings | File Templates.
 */
@Component
public class UserLoader {

    @Bean
    CommandLineRunner initializeUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.findAll()
                    .flatMap(userRepository::delete)
                    .blockLast();

            User user = User.builder()
                    .name("jay")
                    .password(passwordEncoder.encode("jay"))
                    .roles(List.of("ROLE_USER"))
                    .build();
            User admin = User.builder()
                    .name("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles(List.of("ROLE_USER", "ROLE_INVENTORY"))
                    .build();
            Flux.fromIterable(List.of(user, admin))
                    .flatMap(userRepository::save)
                    .blockLast();
        };
    }
}

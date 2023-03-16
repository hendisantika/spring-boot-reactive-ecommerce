package com.hendisantika.springbootreactiveecommerce.repository;

import com.hendisantika.springbootreactiveecommerce.entity.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/17/23
 * Time: 06:22
 * To change this template use File | Settings | File Templates.
 */
public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByName(String name);
}

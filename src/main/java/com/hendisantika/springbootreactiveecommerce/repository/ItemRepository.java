package com.hendisantika.springbootreactiveecommerce.repository;

import com.hendisantika.springbootreactiveecommerce.entity.Item;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/16/23
 * Time: 06:41
 * To change this template use File | Settings | File Templates.
 */
public interface ItemRepository extends ReactiveCrudRepository<Item, String>
        , ReactiveQueryByExampleExecutor<Item> {
}

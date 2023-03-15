package com.hendisantika.springbootreactiveecommerce.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/16/23
 * Time: 06:39
 * To change this template use File | Settings | File Templates.
 */
@Data
public class Item {

    @Id
    private String id;
    private String name;
    private double price;

    public Item() {

    }

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

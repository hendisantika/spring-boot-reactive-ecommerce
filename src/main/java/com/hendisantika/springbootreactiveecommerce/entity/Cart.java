package com.hendisantika.springbootreactiveecommerce.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-reactive-ecommerce
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 3/16/23
 * Time: 06:38
 * To change this template use File | Settings | File Templates.
 */
@Data
public class Cart {

    @Id
    private String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }
}

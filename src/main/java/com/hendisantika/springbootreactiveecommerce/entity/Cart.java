package com.hendisantika.springbootreactiveecommerce.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class Cart {

    @Id
    private String id;
    private List<CartItem> cartItems = new ArrayList<>();

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }

    public List<CartItem> getCartItems() {
        if (this.cartItems == null) {
            this.cartItems = new ArrayList<>();
        }
        return this.cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void setId(String id) {
        this.id = id;
    }
}

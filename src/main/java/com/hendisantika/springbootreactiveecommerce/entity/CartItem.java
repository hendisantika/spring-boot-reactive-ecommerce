package com.hendisantika.springbootreactiveecommerce.entity;

import lombok.Data;

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
public class CartItem {

    private Item item;
    private int quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public void increment() {
        this.quantity += 1;
    }

    public Item getItem() {
        return this.item;
    }

    public int getQuantity() {
        return this.quantity;
    }
}

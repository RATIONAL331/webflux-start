package com.hello.webfluxstart.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CartItem {
    private Item item;
    private Integer quantity;

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public void increment() {
        quantity++;
    }
}

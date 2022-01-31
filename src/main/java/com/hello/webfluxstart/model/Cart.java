package com.hello.webfluxstart.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Cart {
    @Id
    private String id;
    private List<CartItem> cartItems;

    public Cart(String id) {
        this.id = id;
        this.cartItems = new ArrayList<>();
    }
}

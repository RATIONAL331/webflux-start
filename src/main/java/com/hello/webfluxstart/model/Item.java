package com.hello.webfluxstart.model;

import lombok.*;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {
    @Id
    private String id;
    private String name;
    private Double price;

    public Item(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}

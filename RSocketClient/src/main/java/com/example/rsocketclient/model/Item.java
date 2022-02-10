package com.example.rsocketclient.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Item {
    private String id;
    private String name;
    private String description;
    private Double price;

    public Item(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}

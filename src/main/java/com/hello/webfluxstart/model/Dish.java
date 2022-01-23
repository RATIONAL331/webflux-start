package com.hello.webfluxstart.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Dish {
    private String description;
    private Boolean delivered;

    public Dish(String description) {
        this.description = description;
        this.delivered = false;
    }

    public static Dish delivered(Dish dish) {
        Dish deliveredDish = new Dish(dish.getDescription());
        deliveredDish.setDelivered(true);
        return deliveredDish;
    }
}

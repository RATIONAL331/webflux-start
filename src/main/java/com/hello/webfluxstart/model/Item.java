package com.hello.webfluxstart.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Item {
    @Id
    private String id;
    private String name;
    private String description;
    private Double price;
    private String distributorRegion;
    private LocalDate releaseDate;
    private Integer availableUnits;
    private Point location;
    private Boolean active;

    public Item(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public Item(String name, Double price) {
        this.name = name;
        this.description = "EMPTY";
        this.price = price;
    }
}

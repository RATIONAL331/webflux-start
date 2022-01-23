package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Dish;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class KitchenService {
    public Flux<Dish> getDishes() {
        return Flux.<Dish>generate(sink -> sink.next(randomDish())).delayElements(Duration.ofMillis(250));
    }

    private Dish randomDish() {
        return menu.get(picker.nextInt(menu.size()));
    }

    private static final List<Dish> menu = Arrays.asList(
            new Dish("Sesame chicken"),
            new Dish("Lo mein noodles, plain"),
            new Dish("Sweet & Sour beef")
    );

    private static final Random picker = new Random();
}

package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Dish;
import com.hello.webfluxstart.service.KitchenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ServerController {
    private final KitchenService kitchenService;

    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Dish> serveDishes() {
        return this.kitchenService.getDishes();
    }

    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Dish> deliveredDishes() {
        return this.kitchenService.getDishes().map(Dish::delivered);
    }
}

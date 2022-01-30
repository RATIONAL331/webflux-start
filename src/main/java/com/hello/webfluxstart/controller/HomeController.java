package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.repository.CartRepository;
import com.hello.webfluxstart.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    @GetMapping
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("home")
                                  .modelAttribute("items", itemRepository.findAll())
                                  .modelAttribute("cartItems", cartRepository.findById("My Cart")
                                                                             .defaultIfEmpty(new Cart("My Cart"))
                                                                             .map(Cart::getCartItems)
                                                                             .flatMapMany(Flux::fromIterable))
                                  .build());
    }
}

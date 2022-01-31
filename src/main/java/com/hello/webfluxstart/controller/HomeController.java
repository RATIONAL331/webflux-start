package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.repository.CartRepository;
import com.hello.webfluxstart.repository.ItemRepository;
import com.hello.webfluxstart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;

    @GetMapping
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("home")
                                  .modelAttribute("items", itemRepository.findAll())
                                  .modelAttribute("cart", cartRepository.findById("My Cart")
                                                                        .defaultIfEmpty(new Cart("My Cart")))
                                  .modelAttribute("cartItems", cartRepository.findById("My Cart")
                                                                             .defaultIfEmpty(new Cart("My Cart"))
                                                                             .map(Cart::getCartItems)
                                                                             .flatMapMany(Flux::fromIterable))
                                  .build());
    }

    @PostMapping("/add/{id}")
    public Mono<String> addToCart(@PathVariable String id) {
        return cartService.addToCart("My Cart", id)
                          .thenReturn("redirect:/");
    }
}

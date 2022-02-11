package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.service.CartService;
import com.hello.webfluxstart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final CartService cartService;
    private final InventoryService inventoryService;

    @GetMapping
    public Mono<Rendering> home(Authentication authentication) {
        return Mono.just(Rendering.view("home")
                                  .modelAttribute("items", inventoryService.getAllItem())
                                  .modelAttribute("cart", cartService.getCart(Cart.cartName(authentication))
                                                                     .defaultIfEmpty(new Cart(Cart.cartName(authentication))))
                                  .modelAttribute("cartItems", cartService.getCart(Cart.cartName(authentication))
                                                                          .defaultIfEmpty(new Cart(Cart.cartName(authentication)))
                                                                          .map(Cart::getCartItems)
                                                                          .flatMapMany(Flux::fromIterable))
                                  .modelAttribute("auth", authentication)
                                  .build());
    }

    @PostMapping("/add/{id}")
    public Mono<String> addToCart(Authentication authentication, @PathVariable String id) {
        return cartService.addItemToCart(Cart.cartName(authentication), id)
                          .thenReturn("redirect:/");
    }

    @DeleteMapping("/add/{id}")
    public Mono<String> removeFromCart(Authentication authentication, @PathVariable String id) {
        return cartService.removeOneFromCart(Cart.cartName(authentication), id).thenReturn("redirect:/");
    }

    @GetMapping("/search")
    public Mono<Rendering> search(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String description,
                                  @RequestParam Boolean useAnd) {
        return Mono.just(Rendering.view("home")
                                  .modelAttribute("results", inventoryService.searchByExample(name, description, useAnd))
                                  .build());

    }
}

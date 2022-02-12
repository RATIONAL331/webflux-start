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

//    @GetMapping
//    // GCP 승인된 리디렉션으로 http://localhost:{port}/login/oauth2/code/google 설정
//    public Mono<Rendering> home(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient auth2AuthorizedClient,
//                                @AuthenticationPrincipal OAuth2User oAuth2User) {
//        return Mono.just(Rendering.view("home")
//                                  .modelAttribute("items", inventoryService.getAllItem())
//                                  .modelAttribute("cart", cartService.getCart(Cart.cartName(oAuth2User))
//                                                                     .defaultIfEmpty(new Cart(Cart.cartName(oAuth2User))))
//                                  .modelAttribute("cartItems", cartService.getCart(Cart.cartName(oAuth2User))
//                                                                          .defaultIfEmpty(new Cart(Cart.cartName(oAuth2User)))
//                                                                          .map(Cart::getCartItems)
//                                                                          .flatMapMany(Flux::fromIterable))
//                                  .modelAttribute("userName", oAuth2User.getName())
//                                  .modelAttribute("authorities", oAuth2User.getAuthorities())
//                                  .modelAttribute("clientName", auth2AuthorizedClient.getClientRegistration().getClientName())
//                                  .modelAttribute("userAttributes", oAuth2User.getAuthorities())
//                                  .build());
//    }

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

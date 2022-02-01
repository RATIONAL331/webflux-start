package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.CartItem;
import com.hello.webfluxstart.repository.CartRepository;
import com.hello.webfluxstart.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    public Mono<Cart> getCart(String id) {
        return cartRepository.findById("My Cart");
    }

    public Mono<Cart> addToCart(String cartId, String id) {
        return cartRepository.findById(cartId)
                             .defaultIfEmpty(new Cart(cartId))
                             .flatMap(cart -> cart.getCartItems()
                                                  .stream()
                                                  .filter(cartItem -> cartItem.getItem().getId().equals(id))
                                                  .findAny()
                                                  .map(cartItem -> {
                                                      cartItem.increment();
                                                      return Mono.just(cart);
                                                  }).orElseGet(() -> itemRepository.findById(id)
                                                                                   .map(CartItem::new)
                                                                                   .map(cartItem -> {
                                                                                       cart.getCartItems().add(cartItem);
                                                                                       return cart;
                                                                                   })))
                             .flatMap(cartRepository::save);
    }

    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        return cartRepository.findById(cartId).log("foundCart")
                             .defaultIfEmpty(new Cart(cartId)).log("emptyCart", Level.WARNING) // 2번째 매개변수로 레벨 설정 가능
                             .flatMap(cart -> cart.getCartItems()
                                                  .stream()
                                                  .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                                                  .findAny()
                                                  .map(cartItem -> {
                                                      cartItem.increment();
                                                      return Mono.just(cart).log("newCartItem");
                                                  }).orElseGet(() -> itemRepository.findById(itemId).log("fetchedItem")
                                                                                   .map(CartItem::new).log("cartItem")
                                                                                   .map(cartItem -> {
                                                                                       cart.getCartItems().add(cartItem);
                                                                                       return cart;
                                                                                   })
                                                                                   .log("addedCartItem")))
                             .log("cartWithAnotherItem")
                             .flatMap(cartRepository::save)
                             .log("savedCart");
    }

    @Deprecated
    public Mono<Cart> addItemCartBlock(String cartId, String itemId) {
        Cart myCartBlocked = cartRepository.findById(cartId)
                                           .defaultIfEmpty(new Cart(cartId))
                                           .block();

        return myCartBlocked.getCartItems()
                            .stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(itemId))
                            .findAny()
                            .map(cartItem -> {
                                cartItem.increment();
                                return Mono.just(myCartBlocked);
                            }).orElseGet(() -> itemRepository.findById(itemId)
                                                             .map(CartItem::new)
                                                             .map(cartItem -> {
                                                                 myCartBlocked.getCartItems().add(cartItem);
                                                                 return myCartBlocked;
                                                             }))
                            .flatMap(cartRepository::save);
    }

}

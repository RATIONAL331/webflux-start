package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.CartItem;
import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.repository.CartRepository;
import com.hello.webfluxstart.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;
    @InjectMocks
    private CartService cartService;

    private Item testItem;
    private Cart testCart;

    @Test
    public void init() {
    }

    @BeforeEach
    public void setUp() {
        testItem = new Item("TV tray", "Alf TV tray", 19.99);
        CartItem cartItem = new CartItem(testItem);
        testCart = new Cart("My Cart", Collections.singletonList(cartItem));
    }

    @Test
    public void 비어있는_카트에_아이템_하나_집어넣기() {
        Mockito.when(cartRepository.findById(Mockito.anyString())).thenReturn(Mono.empty());
        Mockito.when(itemRepository.findById(Mockito.anyString())).thenReturn(Mono.just(testItem));
        Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(Mono.just(testCart));

        cartService.addItemToCart("My Cart", "item1")
                   .as(StepVerifier::create)
                   .expectNextMatches(cart -> {
                       Assertions.assertThat(cart.getCartItems()).extracting(CartItem::getQuantity).containsExactlyInAnyOrder(1);
                       Item expectedItem = new Item("TV tray", "Alf TV tray", 19.99);
                       Assertions.assertThat(cart.getCartItems()).extracting(CartItem::getItem).containsExactly(expectedItem);
                       return true;
                   })
                   .verifyComplete();
    }
}
package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.CartService;
import com.hello.webfluxstart.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(HomeController.class)
class HomeControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private InventoryService inventoryService;
    @MockBean
    private CartService cartService;

    @Test
    public void init() {
    }

    @Test
    public void homePage() {
        Item testItem1 = new Item("name1", "desc1", 1.99);
        testItem1.setId("id1");
        Item testItem2 = new Item("name2", "desc2", 9.99);
        testItem2.setId("id2");
        Mockito.when(inventoryService.getAllItem()).thenReturn(Flux.just(testItem1, testItem2));
        Mockito.when(cartService.getCart("My Cart")).thenReturn(Mono.just(new Cart("My Cart")));

        webTestClient.get().uri("/").exchange()
                     .expectStatus().isOk()
                     .expectBody(String.class)
                     .consumeWith(stringEntityExchangeResult -> {
                         Assertions.assertThat(stringEntityExchangeResult.getResponseBody()).contains("action=\"/add/id1\"");
                         Assertions.assertThat(stringEntityExchangeResult.getResponseBody()).contains("action=\"/add/id2\"");
                     });
    }

}
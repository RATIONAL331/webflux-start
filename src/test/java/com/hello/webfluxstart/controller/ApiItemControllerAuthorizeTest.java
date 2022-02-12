package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class ApiItemControllerAuthorizeTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private InventoryService inventoryService;

    private Item testItem;

    @BeforeEach
    public void setUp() {
        testItem = new Item("iPhone X", 999.99);
    }

    @Test
    @WithMockUser(username = "alice", roles = {"OTHER_ROLES"})
    public void 허용되지_않은_사람의_삽입() {
        webTestClient.post()
                     .uri("/api/items/add")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(testItem)
                     .exchange()
                     .expectStatus()
                     .isForbidden();
    }

    @Test
    @WithMockUser(username = "bob", roles = {"INVENTORY"})
    public void 허용된_사람의_삽입() {
        webTestClient.post()
                     .uri("/api/items/add")
                     .contentType(MediaType.APPLICATION_JSON)
                     .bodyValue(testItem)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        inventoryService.getItemByName("iPhone X")
                        .as(StepVerifier::create)
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getId()).isNotNull();
                            Assertions.assertThat(item.getName()).isEqualTo("iPhone X");
                            Assertions.assertThat(item.getPrice()).isEqualTo(999.99);
                            return true;
                        })
                        .verifyComplete();
    }
}
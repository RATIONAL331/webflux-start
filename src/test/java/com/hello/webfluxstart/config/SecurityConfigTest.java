package com.hello.webfluxstart.config;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class SecurityConfigTest {
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private InventoryService inventoryService;

    private final Item testedItem = new Item("iPhone", "upgrade", 999.99);

    @Test
    @WithMockUser(username = "alice", roles = {"SOME_OTHER_ROLE"})
    public void 허가받지_않은_사용자의_추가_삽입() {
        webTestClient.post()
                     .uri("/api/items")
                     .bodyValue(testedItem)
                     .exchange()
                     .expectStatus()
                     .isForbidden();
    }

    @Test
    @WithMockUser(username = "bob", roles = {"INVENTORY"})
    public void INVENTORY_롤을_가진_사람이면_등록되어야_함() {
        webTestClient.post()
                     .uri("/api/items")
                     .bodyValue(testedItem)
                     .exchange()
                     .expectStatus()
                     .isCreated();

        inventoryService.getItemByName("iPhone")
                        .as(StepVerifier::create)
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getId()).isNotNull();
                            Assertions.assertThat(item.getName()).isEqualTo("iPhone");
                            Assertions.assertThat(item.getPrice()).isEqualTo(999.99);
                            return true;
                        })
                        .verifyComplete();
    }

}
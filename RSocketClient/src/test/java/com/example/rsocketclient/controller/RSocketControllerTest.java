package com.example.rsocketclient.controller;

import com.example.rsocketclient.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
class RSocketControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void R소켓을_통해서_요청을_날려보고_응답값_확인하기() {
        webTestClient.post()
                     .uri("/items/request-response")
                     .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99))
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody(Item.class)
                     .value(item -> {
                         Assertions.assertThat(item.getId()).isNotNull();
                         Assertions.assertThat(item.getName()).isEqualTo("Alf alarm clock");
                         Assertions.assertThat(item.getDescription()).isEqualTo("nothing important");
                         Assertions.assertThat(item.getPrice()).isEqualTo(19.99);
                     });
    }

    @Test
    public void 모든_아이템_찾아오기() {
        webTestClient.get()
                     .uri("/items/request-stream")
                     .accept(MediaType.APPLICATION_NDJSON)
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .returnResult(Item.class)
                     .getResponseBody()
                     .as(StepVerifier::create)
                     .expectNextMatches(item -> {
                         Assertions.assertThat(item.getId()).isNotNull();
                         Assertions.assertThat(item.getName()).isEqualTo("Alf alarm clock");
                         Assertions.assertThat(item.getPrice()).isEqualTo(19.99);
                         return true;
                     })
                     .expectNextMatches(item -> {
                         Assertions.assertThat(item.getId()).isNotNull();
                         Assertions.assertThat(item.getName()).isEqualTo("Smurf TV tray");
                         Assertions.assertThat(item.getPrice()).isEqualTo(24.99);
                         return true;
                     })
                     .thenCancel()
                     .verify();
    }

    @Test
    public void 실행후_망각_수행하기() {
        webTestClient.post()
                     .uri("/items/fire-and-forget")
                     .bodyValue(new Item("Alf alarm clock", "nothignt important", 19.99))
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .isEmpty();
    }
}
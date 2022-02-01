package com.hello.webfluxstart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
class WebfluxStartApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void contextLoads() {
    }

    @Test
    public void test() {
        webTestClient.get().uri("/").exchange()
                     .expectStatus().isOk()
                     .expectHeader().contentType(MediaType.TEXT_HTML)
                     .expectBody(String.class)
                     .consumeWith(stringEntityExchangeResult -> Assertions.assertThat(stringEntityExchangeResult.getResponseBody()).contains("action=\"/add/"));
    }

}

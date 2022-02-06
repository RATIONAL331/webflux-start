package com.hello.webfluxstart.rabbitMQ;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@AutoConfigureWebTestClient
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@Testcontainers
@ContextConfiguration
public class RabbitTest {
    @Container
    public final static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.7.25-management-alpine");

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InventoryService inventoryService;

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getContainerIpAddress);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    public void init() {

    }

    @Test
    public void 메시지_검증() throws InterruptedException {
        webTestClient.post()
                     .uri("/items")
                     .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99))
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody();

        Thread.sleep(1500L);

        webTestClient.post()
                     .uri("/items")
                     .bodyValue(new Item("Smurf TV tray", "nothing important", 29.99))
                     .exchange()
                     .expectStatus().isCreated()
                     .expectBody();

        Thread.sleep(2000L);

        inventoryService.getAllItem()
                        .as(StepVerifier::create)
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getName()).isEqualTo("Alf alarm clock");
                            Assertions.assertThat(item.getDescription()).isEqualTo("EMPTY");
                            Assertions.assertThat(item.getPrice()).isEqualTo(19.99);
                            return true;
                        })
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getName()).isEqualTo("Smurf TV tray");
                            Assertions.assertThat(item.getDescription()).isEqualTo("EMPTY");
                            Assertions.assertThat(item.getPrice()).isEqualTo(24.99);
                            return true;
                        })
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getName()).isEqualTo("Alf alarm clock");
                            Assertions.assertThat(item.getDescription()).isEqualTo("nothing important");
                            Assertions.assertThat(item.getPrice()).isEqualTo(19.99);
                            return true;
                        })
                        .expectNextMatches(item -> {
                            Assertions.assertThat(item.getName()).isEqualTo("Smurf TV tray");
                            Assertions.assertThat(item.getDescription()).isEqualTo("nothing important");
                            Assertions.assertThat(item.getPrice()).isEqualTo(29.99);
                            return true;
                        })
                        .verifyComplete();
    }
}

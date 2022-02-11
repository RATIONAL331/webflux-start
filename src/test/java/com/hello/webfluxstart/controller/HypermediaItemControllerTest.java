package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = HypermediaItemController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@AutoConfigureRestDocs
class HypermediaItemControllerTest {
    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private InventoryService inventoryService;

    private Item testItem;

    @BeforeEach
    public void setUp() {
        testItem = new Item("Alf alarm clock", "nothing important", 19.99);
        testItem.setId("1");
    }

    @Test
    public void 하나의_아이템_찾기() {
        Mockito.when(inventoryService.getItem("1")).thenReturn(Mono.just(testItem));
        webTestClient.get()
                     .uri("hypermedia/items/1")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     .consumeWith(WebTestClientRestDocumentation.document("findOne-hypermedia",
                                                                          Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                                                          HypermediaDocumentation.links(HypermediaDocumentation.linkWithRel("self").description("이 `Item`에 대한 공식 링크"),
                                                                                                        HypermediaDocumentation.linkWithRel("item").description("`Item` 목록 링크"))));
    }
}
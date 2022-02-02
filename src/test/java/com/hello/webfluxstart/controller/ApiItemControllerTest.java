package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ApiItemController.class)
@AutoConfigureRestDocs
class ApiItemControllerTest {
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
    public void 모든_아이템_찾기() {
        Mockito.when(inventoryService.getAllItem()).thenReturn(Flux.just(testItem));
        webTestClient.get()
                     .uri("/api/items")
                     .exchange()
                     .expectStatus()
                     .isOk()
                     .expectBody()
                     // document 함수는 스프링 레스트 독 정적 메서드 => 문서 생성 기능을 테스트에 추가하는 역할
                     // target/generated-snippets/findAll 이라는 디렉터리가 생기고 요청 결과로 반환되는 JSON 문자열을 보기 편한 형태로 출력한다.
                     .consumeWith(WebTestClientRestDocumentation.document("findAll", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }

    @Test
    public void 새로운_아이템_등록() {
        Mockito.when(inventoryService.saveItem(Mockito.any())).thenReturn(Mono.just(testItem));
        webTestClient.post()
                     .uri("/api/items")
                     .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99))
                     .exchange()
                     .expectStatus()
                     .isCreated()
                     .expectBody()
                     .consumeWith(WebTestClientRestDocumentation.document("post-new-item", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }
}
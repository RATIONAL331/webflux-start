package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.RSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class RSocketController {
    private final RSocketService rSocketService;

    @MessageMapping("newItems.request-response")
    public Mono<Item> requestResponse(Mono<Item> itemMono) {
        return rSocketService.processNewItemViaRSocketRequestResponse(itemMono);
    }

    @MessageMapping("newItems.request-stream")
    public Flux<Item> requestStream() {
        return rSocketService.findItemsViaRSocketRequestStream();
    }

    @MessageMapping("newItems.fire-and-forget")
    public Mono<Void> fireAndForget(Mono<Item> itemMono) {
        return rSocketService.processNewItemViaRSocketFireAndForget(itemMono);
    }

    @MessageMapping("newItems.channel")
    public Flux<Item> channel() {
        return rSocketService.monitorNewItems();
    }
}

package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Item;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class RSocketService {
    private final InventoryService inventoryService;
    private final Sinks.Many<Item> itemFluxSink;

    public RSocketService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
        this.itemFluxSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public Mono<Item> processNewItemViaRSocketRequestResponse(Mono<Item> itemMono) {
        return itemMono.flatMap(item -> inventoryService.saveItem(item)
                                                        .doOnNext(itemFluxSink::tryEmitNext));
    }

    public Flux<Item> findItemsViaRSocketRequestStream() {
        return inventoryService.getAllItem()
                               .doOnNext(itemFluxSink::tryEmitNext);
    }

    public Mono<Void> processNewItemViaRSocketFireAndForget(Mono<Item> itemMono) {
        return itemMono.flatMap(item -> inventoryService.saveItem(item)
                                                        .doOnNext(itemFluxSink::tryEmitNext)
                                                        .then());
    }

    public Flux<Item> monitorNewItems() {
        return itemFluxSink.asFlux();
    }
}

package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApiItemController {
    private final InventoryService inventoryService;

    @GetMapping("/api/items")
    public Flux<Item> findAllItems() {
        return inventoryService.getAllItem();
    }

    @GetMapping("/api/items/{id}")
    public Mono<Item> findOneItem(@PathVariable String id) {
        return inventoryService.getItem(id);
    }

    @PostMapping("/api/items")
    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(inventoryService::saveItem)
                   .map(savedItem -> ResponseEntity.created(URI.create("/api/items/" + savedItem.getId()))
                                                   .body(savedItem));
    }

    @PutMapping("/api/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<Item> item, @PathVariable String id) {
        return item.map(content -> {
                       Item replacedItem = new Item(content.getName(), content.getDescription(), content.getPrice());
                       replacedItem.setId(id);
                       return replacedItem;
                   })
                   .flatMap(inventoryService::saveItem)
                   .map(ResponseEntity::ok);
    }
}

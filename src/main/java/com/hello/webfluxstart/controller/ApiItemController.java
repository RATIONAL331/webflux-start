package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.config.SecurityConfig;
import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasRole('" + SecurityConfig.INVENTORY + "')")
    @PostMapping("/api/items/add")
    public Mono<ResponseEntity<?>> addNewItemAuthorize(@RequestBody Mono<Item> itemMono, Authentication authentication) { // Authentication 주입 가능
        return itemMono.flatMap(inventoryService::saveItem)
                       .map(Item::getId)
                       .flatMap(this::findOneItem)
                       .map(getItem -> ResponseEntity.created(URI.create("/api/items/" + getItem.getId()))
                                                     .body(getItem));
    }

    @PreAuthorize("hasRole('" + SecurityConfig.INVENTORY + "')")
    @DeleteMapping("/api/items/delete/{id}")
    public Mono<ResponseEntity<?>> deleteItemAuthorize(@PathVariable String id) {
        return inventoryService.deleteItem(id)
                               .thenReturn(ResponseEntity.noContent()
                                                         .build());
    }
}

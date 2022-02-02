package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApiItemController {
    private final ItemRepository itemRepository;

    @GetMapping("/api/items")
    public Flux<Item> findAllItems() {
        return itemRepository.findAll();
    }

    @GetMapping("/api/items/{id}")
    public Mono<Item> findOneItem(@PathVariable String id) {
        return itemRepository.findById(id);
    }

    @PostMapping("/api/items")
    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(itemRepository::save)
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
                   .flatMap(itemRepository::save)
                   .map(ResponseEntity::ok);
    }
}

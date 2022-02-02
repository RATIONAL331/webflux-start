package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.hateoas.server.reactive.WebFluxLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class HypermediaItemController {
    private final InventoryService inventoryService;

    @GetMapping("/hypermedia/items/{id}")
    public Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        HypermediaItemController controller = WebFluxLinkBuilder.methodOn(HypermediaItemController.class);
        Mono<Link> selfLink = WebFluxLinkBuilder.linkTo(controller.findOne(id)).withSelfRel().toMono();
        Mono<Link> aggregateLink = WebFluxLinkBuilder.linkTo(controller.findAll()).withRel(IanaLinkRelations.ITEM).toMono();
        return Mono.zip(inventoryService.getItem(id), selfLink, aggregateLink)
                   .map(mono -> EntityModel.of(mono.getT1(), Links.of(mono.getT2(), mono.getT3())));
    }

    @GetMapping("/hypermedia/items")
    public Mono<CollectionModel<EntityModel<Item>>> findAll() {
        return inventoryService.getAllItem()
                               .flatMap(item -> findOne(item.getId()))
                               .collectList()
                               .flatMap(entityModels -> WebFluxLinkBuilder.linkTo(WebFluxLinkBuilder.methodOn(HypermediaItemController.class).findAll())
                                                                          .withSelfRel()
                                                                          .toMono()
                                                                          .map(selfLink -> CollectionModel.of(entityModels, selfLink)));
    }
}

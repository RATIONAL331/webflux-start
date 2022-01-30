package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Item;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {
}

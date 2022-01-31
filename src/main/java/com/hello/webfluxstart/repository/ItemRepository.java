package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Item;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveCrudRepository<Item, String>, ReactiveQueryByExampleExecutor<Item> {
    // name not ignore case
    Flux<Item> findAllByNameContaining(String partialName);

    // name
    Flux<Item> findAllByNameContainingIgnoreCase(String partialName);

    // description
    Flux<Item> findAllByDescriptionContainingIgnoreCase(String partialName);

    // name AND description
    Flux<Item> findAllByNameContainingAndDescriptionContainingAllIgnoreCase(String partialName, String partialDesc);

    // name OR description
    Flux<Item> findAllByNameContainingOrDescriptionContainingAllIgnoreCase(String partialName, String partialDesc);

    @Query("{ 'name' : ?0, 'age' : ?1 }")
    Flux<Item> findItemsForCustomerMonthlyReport(String name, Integer age);

    @Query(sort = "{ 'age' : -1 }")
    Flux<Item> findSortedStuffForWeeklyReport();
}

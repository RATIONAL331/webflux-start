package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.ReactiveFluentMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final ItemRepository itemRepository;
    // ReactiveFluentMongoOperations 는 FluentMongoOperations 의 리액티브 버전을 의미
    private final ReactiveFluentMongoOperations reactiveFluentMongoOperations;
    // private final FluentMongoOperations fluentMongoOperations;

    // 쿼리 메소드만 사용한 매우 복잡한 로직
    public Flux<Item> search(@Nullable String partialName,
                             @Nullable String partialDescription,
                             Boolean useAnd) {
        if (partialName != null) {
            if (partialDescription != null) {
                if (useAnd) {
                    return itemRepository.findAllByNameContainingAndDescriptionContainingAllIgnoreCase(partialName, partialDescription);
                } else {
                    return itemRepository.findAllByNameContainingOrDescriptionContainingAllIgnoreCase(partialName, partialDescription);
                }
            } else {
                return itemRepository.findAllByNameContainingIgnoreCase(partialName);
            }
        } else {
            if (partialDescription != null) {
                return itemRepository.findAllByDescriptionContainingIgnoreCase(partialDescription);
            } else {
                return itemRepository.findAll();
            }
        }
    }

    public Flux<Item> searchByExample(String name, String description, Boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        // UntypedExampleMatcher 는 모든 컬렉션에 대해서 수행하게 되고, ExampleMatcher 는 해당하는 클래스에 맞는 도큐먼트에 대해서만 수행
        // UntypedExampleMatcher untypedExampleMatcher = UntypedExampleMatcher.matchingAll();

        ExampleMatcher exampleMatcher = useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny();
        ExampleMatcher ignoreStringMatcher = exampleMatcher.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                                                           .withIgnoreCase()
                                                           .withIgnorePaths("price");

        Example<Item> probe = Example.of(item, ignoreStringMatcher);

        return itemRepository.findAll(probe);
    }

    public Flux<Item> searchByFluentExample(String name, String description) {
        // FluentMongoOperations 는 비어있는 필드, 부분 일치 기능은 사용할 수 없다.
        return reactiveFluentMongoOperations.query(Item.class)
                                            .matching(Query.query(Criteria.where("TV tray")
                                                                          .is(name)
                                                                          .and("Smurf")
                                                                          .is(description)))
                                            // { $and : [ { name : 'TV tray' }, { description : 'Smurf' } ] }
                                            .all();
    }

    public Flux<Item> searchFluentExample(String name, String description, Boolean useAnd) {
        Item item = new Item(name, description, 0.0);

        ExampleMatcher exampleMatcher = useAnd ? ExampleMatcher.matchingAll() : ExampleMatcher.matchingAny();
        ExampleMatcher ignoreStringMatcher = exampleMatcher.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                                                           .withIgnoreCase()
                                                           .withIgnorePaths("price");

        return reactiveFluentMongoOperations.query(Item.class)
                                            .matching(Query.query(Criteria.byExample(Example.of(item, ignoreStringMatcher))))
                                            .all();
    }
}

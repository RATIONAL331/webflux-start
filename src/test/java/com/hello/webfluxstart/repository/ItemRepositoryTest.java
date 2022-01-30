package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.TestPropertySource;

@DataMongoTest
// https://stackoverflow.com/questions/70047380/enableautoconfigurationexclude-on-tests-failed-in-spring-boot-2-6-0
// https://www.inflearn.com/questions/370982
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
public class ItemRepositoryTest {
    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;
    @Autowired
    ItemRepository repository;

    @Test
    public void 하나를_삽입하기() {
        repository.save(new Item("Alf alarm clock", 19.99)).subscribe();
    }
}
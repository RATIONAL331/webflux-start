package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.CartItem;
import com.hello.webfluxstart.model.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class TemplateDatabaseLoader {
    @Bean
    public CommandLineRunner initialize(MongoOperations mongoOperations) {
        return args -> {
            mongoOperations.findAllAndRemove(new Query(), Item.class);
            mongoOperations.findAllAndRemove(new Query(), CartItem.class);
            mongoOperations.findAllAndRemove(new Query(), Cart.class);
            mongoOperations.save(new Item("Alf alarm clock", 19.99));
            mongoOperations.save(new Item("Smurf TV tray", 24.99));
        };
    }
}

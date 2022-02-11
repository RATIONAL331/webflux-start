package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.CartItem;
import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class TemplateDatabaseLoader {
    @Bean
    public CommandLineRunner initialize(MongoOperations mongoOperations) {
        return args -> {
            mongoOperations.findAllAndRemove(new Query(), Item.class);
            mongoOperations.findAllAndRemove(new Query(), CartItem.class);
            mongoOperations.findAllAndRemove(new Query(), Cart.class);
            mongoOperations.findAllAndRemove(new Query(), User.class);
            mongoOperations.save(new User("ryungjin.kim", "this_is_password", Collections.singletonList("ROLE_USER")));
            mongoOperations.save(new Item("Alf alarm clock", 19.99));
            mongoOperations.save(new Item("Smurf TV tray", 24.99));
        };
    }
}

package com.hello.webfluxstart.repository;

import com.hello.webfluxstart.model.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartRepository extends ReactiveCrudRepository<Cart, String> {
}

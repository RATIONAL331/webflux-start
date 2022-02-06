package com.hello.webfluxstart.service;

import com.hello.webfluxstart.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpringAmqpItemService {
    private final InventoryService inventoryService;

    @RabbitListener(
            ackMode = "MANUAL",
            bindings = @QueueBinding(
                    value = @Queue,
                    exchange = @Exchange("hacking-spring-boot"),
                    key = "new-items-spring-amqp"
            )
    )
    public Mono<Void> processNewItemViaSpringAmqp(Item item) {
        return inventoryService.saveItem(item).then();
    }

}

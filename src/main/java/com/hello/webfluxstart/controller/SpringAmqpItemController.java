package com.hello.webfluxstart.controller;

import com.hello.webfluxstart.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SpringAmqpItemController {
    private final AmqpTemplate amqpTemplate;

    /**
     * 많은 래빗엠큐 API는 작업 수행 중 현재 스레드를 블록한다.
     * 작업 수행 단계 중 블록킹 API 호출이 포함 된다면 리액터에게 이를 알려줘 별도의 스레드에서 호출하게 해야 스레드 낭비를 방지할 수 있다.
     * Schedulers#immediate: 현재 스레드
     * Schedulers#single: 재사용 가능한 하나의 스레드, 현재 수행 중인 리액터 플로우 뿐만 아니라 호출되는 모든 작업이 동일한 하나의 스레드에서 실행됨
     * Schedulers#newSingle: 새로 생성한 전용 스레드
     * Schedulers#boundedElastic: 작업량에 따라 스레드 숫자가 늘어나거나 줄어드는 신축성 스레드풀 사용
     * Schedulers#parallel: 병렬 작업에 최적화된 고정 크기 워커 스레드풀 사용
     * Schedulers#fromExecutorService: ExecutorService 인스턴스를 감싸서 재사용
     * single, newSingle, parallel은 논블록킹 작업에 사용되는 스레드를 생성한다. 이 세가지는 블록킹 코드가 사용되면 IllegalStateException이 발생된다.
     */

    /**
     * 리액터 플로우에서 스케줄러를 변경하는 방법은 두가지
     * publishOn: 호출되는 시점 이후로는 지정한 스케줄러 사용. 이 방법은 사용하는 스케줄러를 여러 번 바꿀 수 있다.
     * subscribeOn: 플로우 전 단계에 걸쳐 사용되는 스케줄러 지정. 플로우 전체에 영향을 미치므로 publishOn에 비해 영향 범위가 넓다.
     * 리액터 플로우에서 subscribeOn이 어디에 위치하든 해당 플로우 전체가 subscribeOn으로 지정한 스레드에서 실행됨
     * 나중에 publishOn으로 스레드를 다시 지정하면 지정한 지점 이후부터는 publishOn으로 새로 지정한 스레드에서 리액터 플로우 수행
     */

    @PostMapping("/items")
    public Mono<ResponseEntity<?>> addNewItemUsingSpringAmqp(@RequestBody Mono<Item> itemMono) {
        return itemMono.subscribeOn(Schedulers.boundedElastic())
                       .flatMap(content -> Mono.fromCallable(() -> {
                           amqpTemplate.convertAndSend("hacking-spring-boot", "new-items-spring-amqp", content);
                           return ResponseEntity.created(URI.create("/items"))
                                                .build();
                       }));
    }
}

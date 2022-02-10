package com.example.rsocketclient.controller;

import com.example.rsocketclient.model.Item;
import io.rsocket.metadata.WellKnownMimeType;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.URI;
import java.time.Duration;

@RestController
public class RSocketController {
    private final Mono<RSocketRequester> rSocketRequester;

    public RSocketController(RSocketRequester.Builder builder) {
        this.rSocketRequester = Mono.just(builder.rsocketConnector(connector -> connector.reconnect(Retry.max(5)))
                                                 .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                                                 .metadataMimeType(MediaType.parseMediaType(WellKnownMimeType.MESSAGE_RSOCKET_ROUTING.getString()))
                                                 .tcp("127.0.0.1", 7000))
                                    .cache();
    }

    @PostMapping("/items/request-response")
    public Mono<ResponseEntity<?>> addNewItemUsingRSocketRequestResponse(@RequestBody Mono<Item> itemMono) {
        return itemMono.flatMap(item -> rSocketRequester.flatMap(requester -> requester.route("newItems.request-response")
                                                                                       .data(item)
                                                                                       .retrieveMono(Item.class))
                                                        .map(savedItem -> ResponseEntity.created(URI.create("/items/request-response"))
                                                                                        .body(savedItem)));
    }

    @GetMapping(value = "/items/request-stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<Item> findItemsUsingRSocketRequestStream() {
        return rSocketRequester.flatMapMany(requester -> requester.route("newItems.request-stream")
                                                                  .retrieveFlux(Item.class)
                                                                  .delayElements(Duration.ofSeconds(1)));
    }

    @PostMapping("/items/fire-and-forget")
    public Mono<ResponseEntity<?>> addNewItemUsingRSocketFIreAndForget(@RequestBody Mono<Item> itemMono) {
        return itemMono.flatMap(item -> rSocketRequester.flatMap(requester -> requester.route("newItems.fire-and-forget")
                                                                                       .data(item)
                                                                                       .send()) // 반환값이 Mono<Void> 형태이므로 map, flatMap 변환이 무시된다. 따라서 then으로 체이닝을 한다.
                                                        .then(Mono.just(ResponseEntity.created(URI.create("/items/fire-and-forget"))
                                                                                      .build())));
    }

    @GetMapping(value = "/items", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Item> liveUpdates() {
        return rSocketRequester.flatMapMany(requester -> requester.route("newItems.channel")
                                                                  .retrieveFlux(Item.class));
    }
}

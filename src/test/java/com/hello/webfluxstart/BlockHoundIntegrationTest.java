package com.hello.webfluxstart;

import com.hello.webfluxstart.model.Cart;
import com.hello.webfluxstart.model.CartItem;
import com.hello.webfluxstart.model.Item;
import com.hello.webfluxstart.repository.CartRepository;
import com.hello.webfluxstart.repository.ItemRepository;
import com.hello.webfluxstart.service.CartService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class BlockHoundIntegrationTest {
    @InjectMocks
    private CartService cartService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    public void setUp() {
        Item testItem = new Item("TV tray", "Alf TV tray", 19.99);
        CartItem cartItem = new CartItem(testItem);
        Cart testCart = new Cart("My Cart", Collections.singletonList(cartItem));

        Mockito.when(cartRepository.findById(Mockito.anyString())).thenReturn(Mono.<Cart>empty().hide()); // hide의 주 목적 => 진단을 정확하기 위해 '식별성 기준 최적화'를 방지한다.
        Mockito.lenient().when(itemRepository.findById(Mockito.anyString())).thenReturn(Mono.just(testItem));
        Mockito.lenient().when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(Mono.just(testCart));
    }

    /**
     * java.lang.AssertionError: expectation "expectComplete" failed (expected: onComplete(); actual: onError(java.lang.IllegalStateException: block()/blockFirst()/blockLast() are blocking, which is not supported in thread parallel-1))
     * <p>
     * at reactor.test.MessageFormatter.assertionError(MessageFormatter.java:115)
     * at reactor.test.MessageFormatter.failPrefix(MessageFormatter.java:104)
     * at reactor.test.MessageFormatter.fail(MessageFormatter.java:73)
     * at reactor.test.MessageFormatter.failOptional(MessageFormatter.java:88)
     * at reactor.test.DefaultStepVerifierBuilder.lambda$expectComplete$4(DefaultStepVerifierBuilder.java:336)
     * at reactor.test.DefaultStepVerifierBuilder$SignalEvent.test(DefaultStepVerifierBuilder.java:2218)
     * at reactor.test.DefaultStepVerifierBuilder$DefaultVerifySubscriber.onSignal(DefaultStepVerifierBuilder.java:1490)
     * at reactor.test.DefaultStepVerifierBuilder$DefaultVerifySubscriber.onExpectation(DefaultStepVerifierBuilder.java:1438)
     * at reactor.test.DefaultStepVerifierBuilder$DefaultVerifySubscriber.onError(DefaultStepVerifierBuilder.java:1091)
     * at reactor.core.publisher.MonoFlatMap$FlatMapMain.onNext(MonoFlatMap.java:129)
     */

    @Test
    public void blockHoundShouldTrapBlockingCall() {
        Mono.delay(Duration.ofSeconds(1))
            .flatMap(tick -> cartService.addItemCartBlock("My Cart", "item1"))
            .as(StepVerifier::create)
            .verifyErrorSatisfies(throwable -> Assertions.assertThat(throwable).hasMessageContaining("block()/blockFirst()/blockLast() are blocking"));
    }
}

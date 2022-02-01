package com.hello.webfluxstart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class BlockHoundUnitTest {
    // 블록하운드는 Thread#sleep, Socket, 네트워크 연산, 파일 메소드 사용등을 검출해낼 수 있다.

    /**
     * java.lang.AssertionError: expectation "expectComplete" failed (expected: onComplete(); actual: onError(reactor.blockhound.BlockingOperationError: Blocking call! java.lang.Thread.sleep))
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
    public void 블록킹_호출() {
        Mono.delay(Duration.ofSeconds(1))
            .flatMap(tick -> {
                try {
                    Thread.sleep(10);
                    return Mono.just(true);
                } catch (InterruptedException e) {
                    return Mono.error(e);
                }
            })
            .as(StepVerifier::create)
            .verifyErrorMatches(throwable -> {
                Assertions.assertThat(throwable.getMessage()).contains("Blocking call! java.lang.Thread.sleep");
                return true;
            });
    }
}

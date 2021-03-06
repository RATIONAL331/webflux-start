package com.hello.webfluxstart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class ReactorDebugging {
    /**
     * java.lang.IndexOutOfBoundsException: source had 4 elements, expected at least 6
     * <p>
     * at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.onComplete(MonoElementAt.java:165)
     * at reactor.core.publisher.FluxArray$ArraySubscription.fastPath(FluxArray.java:177)
     * at reactor.core.publisher.FluxArray$ArraySubscription.request(FluxArray.java:97)
     * at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.request(MonoElementAt.java:103)
     * at reactor.core.publisher.MonoSubscribeOn$SubscribeOnSubscriber.trySchedule(MonoSubscribeOn.java:189)
     * at reactor.core.publisher.MonoSubscribeOn$SubscribeOnSubscriber.onSubscribe(MonoSubscribeOn.java:134)
     * at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.onSubscribe(MonoElementAt.java:118)
     * at reactor.core.publisher.FluxArray.subscribe(FluxArray.java:53)
     * at reactor.core.publisher.FluxArray.subscribe(FluxArray.java:59)
     * at reactor.core.publisher.Mono.subscribe(Mono.java:4400)
     * at reactor.core.publisher.MonoSubscribeOn$SubscribeOnSubscriber.run(MonoSubscribeOn.java:126)
     * at reactor.core.scheduler.WorkerTask.call(WorkerTask.java:84)
     * at reactor.core.scheduler.WorkerTask.call(WorkerTask.java:37)
     * at java.base/java.util.concurrent.FutureTask.run(FutureTask.java:264)
     * at java.base/java.util.concurrent.ScheduledThreadPoolExecutor$ScheduledFutureTask.run(ScheduledThreadPoolExecutor.java:304)
     * at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
     * at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
     * at java.base/java.lang.Thread.run(Thread.java:834)
     * Suppressed: java.lang.Exception: #block terminated with an error
     * at reactor.core.publisher.BlockingSingleSubscriber.blockingGet(BlockingSingleSubscriber.java:99)
     * at reactor.core.publisher.Mono.block(Mono.java:1707)
     * at com.hello.webfluxstart.ReactorDebugging.????????????_?????????_????????????_??????(ReactorDebugging.java:19)
     * at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
     * at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
     * at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
     * at java.base/java.lang.reflect.Method.invoke(Method.java:566)
     * ...
     */
    @Test
    public void ????????????_?????????_????????????_??????() {
        Mono<Integer> source = Flux.just(1, 2, 3, 4).elementAt(5); // ?????? ??????
        Assertions.assertThatThrownBy(() -> source.subscribeOn(Schedulers.parallel()).block()).isInstanceOf(IndexOutOfBoundsException.class);

    }

    @Test
    public void ????????????_?????????_????????????_??????_???????????????_??????() {
        /**
         * java.lang.IndexOutOfBoundsException: source had 4 elements, expected at least 6
         *
         * 	at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.onComplete(MonoElementAt.java:165)
         * 	Suppressed: The stacktrace has been enhanced by Reactor, refer to additional information below:
         * Assembly trace from producer [reactor.core.publisher.MonoElementAt] :
         * 	reactor.core.publisher.Flux.elementAt(Flux.java:4868)
         * 	com.hello.webfluxstart.ReactorDebugging.????????????_?????????_????????????_??????_???????????????_??????(ReactorDebugging.java:74)
         * Error has been observed at the following site(s):
         * 	*____Flux.elementAt ??? at com.hello.webfluxstart.ReactorDebugging.????????????_?????????_????????????_??????_???????????????_??????(ReactorDebugging.java:74) =========================================================> (1) ???????????? ??????
         * 	|_ Mono.subscribeOn ??? at com.hello.webfluxstart.ReactorDebugging.????????????_?????????_????????????_??????_???????????????_??????(ReactorDebugging.java:75) =========================================================> (2) ???????????? ??????
         * Original Stack Trace:
         * 		at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.onComplete(MonoElementAt.java:165)
         * 		at reactor.core.publisher.FluxArray$ArraySubscription.fastPath(FluxArray.java:177)
         * 		at reactor.core.publisher.FluxArray$ArraySubscription.request(FluxArray.java:97)
         * 		at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.request(MonoElementAt.java:103)
         * 		at reactor.core.publisher.MonoSubscribeOn$SubscribeOnSubscriber.trySchedule(MonoSubscribeOn.java:189)
         * 		at reactor.core.publisher.MonoSubscribeOn$SubscribeOnSubscriber.onSubscribe(MonoSubscribeOn.java:134)
         * 		at reactor.core.publisher.MonoElementAt$ElementAtSubscriber.onSubscribe(MonoElementAt.java:118)
         * ...
         */
        // ?????? ??????????????? ????????? ??????????????? ?????????. ????????? ????????? ???????????? ????????? ???
        Hooks.onOperatorDebug(); // ????????? ??????????????? ??????

        Mono<Integer> source = Flux.just(1, 2, 3, 4).elementAt(5); // ?????? ??????
        Assertions.assertThatThrownBy(() -> source.subscribeOn(Schedulers.parallel()).block()).isInstanceOf(IndexOutOfBoundsException.class);
    }
}

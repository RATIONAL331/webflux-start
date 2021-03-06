package com.hello.webfluxstart.config;

import com.google.auto.service.AutoService;
import com.mongodb.internal.connection.InternalStreamConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.rsocket.context.RSocketServerBootstrap;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.server.reactive.WebFluxLinkBuilder;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;
import reactor.blockhound.integration.BlockHoundIntegration;

@AutoService(BlockHoundIntegration.class)
public class BlockHoundThymeleafConfig implements BlockHoundIntegration {
    @Override
    public void applyTo(BlockHound.Builder builder) {
        builder.allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "initialize");
        builder.allowBlockingCallsInside(TemplateEngine.class.getCanonicalName(), "process");
        builder.allowBlockingCallsInside(InternalStreamConnection.class.getCanonicalName(), "readAsync");
        builder.allowBlockingCallsInside(WebFluxLinkBuilder.class.getCanonicalName(), "linkTo");
        builder.allowBlockingCallsInside(WebFluxLinkBuilder.class.getCanonicalName(), "methodOn");
        builder.allowBlockingCallsInside(MessageSourceAccessor.class.getCanonicalName(), "getMessage");
        builder.allowBlockingCallsInside(RSocketServerBootstrap.class.getCanonicalName(), "start");
        builder.allowBlockingCallsInside(SpringApplication.class.getCanonicalName(), "run");
    }
}

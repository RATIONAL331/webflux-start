package com.hello.webfluxstart.config;

import com.google.auto.service.AutoService;
import com.mongodb.internal.connection.InternalStreamConnection;
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
    }
}

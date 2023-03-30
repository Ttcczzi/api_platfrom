package com.wt.project.handler;

import com.wt.project.common.ErrorInterface;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.net.ConnectException;
import java.util.Map;

@Slf4j
@Order(-1)
@Component
public class CustomWebExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if(ex instanceof ConnectException){
            Map<String, Object> attributes = exchange.getAttributes();
            Long interfaceId = (Long)attributes.get("interfaceId");
            ErrorInterface.checkErrorCount(interfaceId);
        }
        log.error(ex.getMessage());
        return null;
    }
}
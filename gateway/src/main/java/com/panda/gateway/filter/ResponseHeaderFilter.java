package com.panda.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ResponseHeaderFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            ServerHttpResponse response = exchange.getResponse();
            // Xóa header GATEWAY_AUTH nếu có
            response.getHeaders().remove("GATEWAY_AUTH");
            
            // Thêm thời gian hệ thống
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
            response.getHeaders().add("X-System-Time", currentTime);
        }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
} 
package com.panda.gateway.security;

import com.panda.gateway.service.InternalTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class GatewayAuthFilter extends AbstractGatewayFilterFactory<GatewayAuthFilter.Config> {

    @Autowired
    private InternalTokenService internalTokenService;

    public GatewayAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            try {
                String originalJwt = exchange.getRequest().getHeaders()
                        .getFirst("Authorization");

                String username = "GATEWAY";
                if (originalJwt != null) {
                    username = "USER";
                }

                String internalToken = internalTokenService.generateInternalToken(
                        username,
                        originalJwt != null ? originalJwt : ""
                );

                // Tạo request mới với internal token header
                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                        .header("X-Internal-Token", internalToken)
                        .header("X-Gateway-Service", "GATEWAY")
                        .header("X-Request-ID", java.util.UUID.randomUUID().toString())
                        .build();

                // Tạo exchange mới với request đã được modify
                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(modifiedRequest)
                        .build();

                return chain.filter(modifiedExchange);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate internal token", e);
            }
        };
    }

    public static class Config {
        // Cấu hình nếu cần
    }
}
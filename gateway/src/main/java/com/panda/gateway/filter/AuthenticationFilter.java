package com.panda.gateway.filter;

import com.panda.gateway.proto.AuthServiceGrpc;
import com.panda.gateway.proto.TokenRequest;
import com.panda.gateway.proto.TokenResponse;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {

    @GrpcClient("auth-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceStub;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // Bỏ qua xác thực cho các endpoint của auth-service
        if (request.getURI().getPath().contains("/api/auth/")) {
            return chain.filter(exchange);
        }

        // Lấy token từ header
        String token = request.getHeaders().getFirst("Authorization");
        
        if (token == null || token.isEmpty()) {
            return onError(exchange, "No token found", HttpStatus.UNAUTHORIZED);
        }

        // Gọi auth-service để validate token
        return validateToken(token)
                .flatMap(isValid -> {
                    if (isValid) {
                        return chain.filter(exchange);
                    } else {
                        return onError(exchange, "Invalid token", HttpStatus.UNAUTHORIZED);
                    }
                });
    }

    private Mono<Boolean> validateToken(String token) {
        return Mono.fromCallable(() -> {
            try {
                TokenRequest request = TokenRequest.newBuilder()
                        .setToken(token)
                        .build();
                
                TokenResponse response = authServiceStub.validateToken(request);
                return response.getValid();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100; // High priority
    }
} 
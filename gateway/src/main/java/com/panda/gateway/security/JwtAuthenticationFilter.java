package com.panda.gateway.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@Slf4j
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtService jwtService;
    private final InternalTokenService internalTokenService;

    public JwtAuthenticationFilter(JwtService jwtService, InternalTokenService internalTokenService) {
        this.jwtService = jwtService;
        this.internalTokenService = internalTokenService;
    }

    @Override
    @NonNull
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        log.debug("Processing request to path: {}", path);

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid Authorization header found for path: {}", path);
            return chain.filter(exchange);
        }

        String jwt = authHeader.substring(7);

        try {
            if (jwtService.validateToken(jwt)) {
                String username = jwtService.extractUsername(jwt);
                log.debug("Valid JWT token found for user: {} accessing path: {}", username, path);

                // Generate internal token for downstream services
                String internalToken = internalTokenService.generateInternalToken(username, jwt);

                // Add internal token to request headers
                ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(builder -> builder
                        .header("X-Internal-Auth", internalToken)
                        .header("X-Original-JWT", jwt)
                        .header("X-Username", username)
                    )
                    .build();

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

                return chain.filter(modifiedExchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
            } else {
                log.warn("Invalid JWT token for path: {}", path);
            }
        } catch (Exception e) {
            log.error("Error processing JWT token for path: {}, error: {}", path, e.getMessage());
        }

        return chain.filter(exchange);
    }
}
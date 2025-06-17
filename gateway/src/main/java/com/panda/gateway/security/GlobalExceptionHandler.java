package com.panda.gateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(-1)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        log.error("Global exception handler caught exception: ", ex);

        if (ex instanceof AuthenticationException) {
            return handleAuthenticationException(exchange, (AuthenticationException) ex);
        } else if (ex instanceof AccessDeniedException) {
            return handleAccessDeniedException(exchange, (AccessDeniedException) ex);
        } else if (ex instanceof ResponseStatusException) {
            return handleResponseStatusException(exchange, (ResponseStatusException) ex);
        } else if (ex instanceof IllegalArgumentException) {
            return handleBadRequestException(exchange, ex);
        } else if (ex instanceof RuntimeException) {
            return handleRuntimeException(exchange, (RuntimeException) ex);
        }

        return handleGenericException(exchange, ex);
    }

    private Mono<Void> handleAuthenticationException(ServerWebExchange exchange, AuthenticationException ex) {
        return writeErrorResponse(exchange, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED",
                "Authentication failed: " + ex.getMessage());
    }

    private Mono<Void> handleAccessDeniedException(ServerWebExchange exchange, AccessDeniedException ex) {
        return writeErrorResponse(exchange, HttpStatus.FORBIDDEN, "ACCESS_DENIED",
                "Access denied: " + ex.getMessage());
    }

    private Mono<Void> handleResponseStatusException(ServerWebExchange exchange, ResponseStatusException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        return writeErrorResponse(exchange, status, "RESPONSE_STATUS_ERROR",
                ex.getReason() != null ? ex.getReason() : "Response status error");
    }

    private Mono<Void> handleBadRequestException(ServerWebExchange exchange, Throwable ex) {
        return writeErrorResponse(exchange, HttpStatus.BAD_REQUEST, "BAD_REQUEST",
                "Bad request: " + ex.getMessage());
    }

    private Mono<Void> handleRuntimeException(ServerWebExchange exchange, RuntimeException ex) {
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "RUNTIME_ERROR",
                "Runtime error: " + ex.getMessage());
    }

    private Mono<Void> handleGenericException(ServerWebExchange exchange, Throwable ex) {
        return writeErrorResponse(exchange, HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Internal server error: " + ex.getMessage());
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorCode, String message) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorResponse = createErrorResponse(exchange, status, errorCode, message);

        try {
            String body = objectMapper.writeValueAsString(errorResponse);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error serializing error response", e);
            String fallbackBody = "{\"error\":\"SERIALIZATION_ERROR\",\"message\":\"Error creating error response\"}";
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(fallbackBody.getBytes(StandardCharsets.UTF_8));
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }

    private Map<String, Object> createErrorResponse(ServerWebExchange exchange, HttpStatus status, String errorCode, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("path", exchange.getRequest().getPath().value());
        errorResponse.put("method", exchange.getRequest().getMethod().name());
        errorResponse.put("service", "GATEWAY-SERVICE");
        return errorResponse;
    }
}

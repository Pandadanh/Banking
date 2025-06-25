package com.panda.mdmService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAuthenticationException(
            AuthenticationException ex, ServerWebExchange exchange) {
        log.error("Authentication exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED",
                "Authentication failed: " + ex.getMessage(), exchange));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleAccessDeniedException(
            AccessDeniedException ex, ServerWebExchange exchange) {
        log.error("Access denied exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED",
                "Access denied: " + ex.getMessage(), exchange));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserNotFoundException(
            UserNotFoundException ex, ServerWebExchange exchange) {
        log.error("User not found exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.NOT_FOUND, "USER_NOT_FOUND",
                ex.getMessage(), exchange));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, ServerWebExchange exchange) {
        log.error("User already exists exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS",
                ex.getMessage(), exchange));
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleInvalidUserDataException(
            InvalidUserDataException ex, ServerWebExchange exchange) {
        log.error("Invalid user data exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_USER_DATA",
                ex.getMessage(), exchange));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(
            WebExchangeBindException ex, ServerWebExchange exchange) {
        log.error("Validation exception: ", ex);

        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = createErrorResponseMap(HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED", "Validation failed", exchange);
        errorResponse.put("validationErrors", validationErrors);

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleIllegalArgumentException(
            IllegalArgumentException ex, ServerWebExchange exchange) {
        log.error("Illegal argument exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT",
                "Invalid argument: " + ex.getMessage(), exchange));
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleRuntimeException(
            RuntimeException ex, ServerWebExchange exchange) {
        log.error("Runtime exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "RUNTIME_ERROR",
                "Runtime error: " + ex.getMessage(), exchange));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(
            Exception ex, ServerWebExchange exchange) {
        log.error("Generic exception: ", ex);
        return Mono.just(createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR",
                "Internal server error: " + ex.getMessage(), exchange));
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String errorCode,
            String message, ServerWebExchange exchange) {
        Map<String, Object> errorResponse = createErrorResponseMap(status, errorCode, message, exchange);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private Map<String, Object> createErrorResponseMap(HttpStatus status, String errorCode,
            String message, ServerWebExchange exchange) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("path", exchange.getRequest().getURI().getPath());
        errorResponse.put("method", exchange.getRequest().getMethod().name());
        errorResponse.put("service", "MDM-SERVICE");
        return errorResponse;
    }
}

package com.panda.mdmService.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(
            AuthenticationException ex, HttpServletRequest request) {
        log.error("Authentication exception: ", ex);
        return createErrorResponse(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED", 
                "Authentication failed: " + ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(
            AccessDeniedException ex, HttpServletRequest request) {
        log.error("Access denied exception: ", ex);
        return createErrorResponse(HttpStatus.FORBIDDEN, "ACCESS_DENIED", 
                "Access denied: " + ex.getMessage(), request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {
        log.error("User not found exception: ", ex);
        return createErrorResponse(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", 
                ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, HttpServletRequest request) {
        log.error("User already exists exception: ", ex);
        return createErrorResponse(HttpStatus.CONFLICT, "USER_ALREADY_EXISTS", 
                ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUserDataException(
            InvalidUserDataException ex, HttpServletRequest request) {
        log.error("Invalid user data exception: ", ex);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_USER_DATA", 
                ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation exception: ", ex);
        
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = createErrorResponseMap(HttpStatus.BAD_REQUEST, 
                "VALIDATION_FAILED", "Validation failed", request);
        errorResponse.put("validationErrors", validationErrors);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, HttpServletRequest request) {
        log.error("Illegal argument exception: ", ex);
        return createErrorResponse(HttpStatus.BAD_REQUEST, "INVALID_ARGUMENT", 
                "Invalid argument: " + ex.getMessage(), request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {
        log.error("Runtime exception: ", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "RUNTIME_ERROR", 
                "Runtime error: " + ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        log.error("Generic exception: ", ex);
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", 
                "Internal server error: " + ex.getMessage(), request);
    }

    private ResponseEntity<Map<String, Object>> createErrorResponse(HttpStatus status, String errorCode, 
            String message, HttpServletRequest request) {
        Map<String, Object> errorResponse = createErrorResponseMap(status, errorCode, message, request);
        return ResponseEntity.status(status).body(errorResponse);
    }

    private Map<String, Object> createErrorResponseMap(HttpStatus status, String errorCode, 
            String message, HttpServletRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());
        errorResponse.put("method", request.getMethod());
        errorResponse.put("service", "MDM-SERVICE");
        return errorResponse;
    }
}

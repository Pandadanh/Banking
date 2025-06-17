# Global Exception Handling Documentation

## Overview
Comprehensive global exception handling has been implemented across all 3 microservices (Gateway, AuthenService, MdmService) to provide consistent error responses and proper error logging.

## Architecture

### 1. Gateway Service (Reactive)
**File**: `gateway/src/main/java/com/panda/gateway/security/GlobalExceptionHandler.java`

**Features**:
- Implements `ErrorWebExceptionHandler` for reactive error handling
- Handles authentication, authorization, and generic exceptions
- Returns structured JSON error responses
- Includes request path, method, timestamp, and service identifier

**Handled Exceptions**:
- `AuthenticationException` → 401 Unauthorized
- `AccessDeniedException` → 403 Forbidden  
- `ResponseStatusException` → Respective status code
- `IllegalArgumentException` → 400 Bad Request
- `RuntimeException` → 500 Internal Server Error
- Generic `Exception` → 500 Internal Server Error

### 2. AuthenService (Servlet)
**File**: `authenService/src/main/java/com/panda/authenService/exception/GlobalExceptionHandler.java`

**Features**:
- Uses `@RestControllerAdvice` for servlet-based error handling
- Handles authentication, validation, and custom business exceptions
- Provides detailed validation error messages
- Includes service identifier and request metadata

**Custom Exceptions**:
- `AuthenticationFailedException` → 401 Unauthorized
- `UserAlreadyExistsException` → 409 Conflict
- `InvalidTokenException` → 401 Unauthorized

**Handled Exceptions**:
- `AuthenticationException` → 401 Unauthorized
- `BadCredentialsException` → 401 Unauthorized
- `AccessDeniedException` → 403 Forbidden
- `MethodArgumentNotValidException` → 400 Bad Request (with field details)
- `IllegalArgumentException` → 400 Bad Request
- `RuntimeException` → 500 Internal Server Error
- Generic `Exception` → 500 Internal Server Error

### 3. MdmService (Servlet)
**File**: `mdmService/src/main/java/com/panda/mdmService/exception/GlobalExceptionHandler.java`

**Features**:
- Uses `@RestControllerAdvice` for servlet-based error handling
- Handles user management specific exceptions
- Provides validation error details
- Includes service identifier and request metadata

**Custom Exceptions**:
- `UserNotFoundException` → 404 Not Found
- `UserAlreadyExistsException` → 409 Conflict
- `InvalidUserDataException` → 400 Bad Request

**Handled Exceptions**:
- `AuthenticationException` → 401 Unauthorized
- `AccessDeniedException` → 403 Forbidden
- `MethodArgumentNotValidException` → 400 Bad Request (with field details)
- `IllegalArgumentException` → 400 Bad Request
- `RuntimeException` → 500 Internal Server Error
- Generic `Exception` → 500 Internal Server Error

## Error Response Format

All services return consistent error response format:

```json
{
  "timestamp": "2024-06-17T10:30:45",
  "status": 400,
  "error": "Bad Request",
  "errorCode": "VALIDATION_FAILED",
  "message": "Validation failed",
  "path": "/api/users",
  "method": "POST",
  "service": "MDM-SERVICE",
  "validationErrors": {
    "userId": "User ID is required",
    "phone": "Phone number must be 10-11 digits"
  }
}
```

## Validation

### AuthenService DTOs
- `LoginRequest`: Username and password validation
- `RegisterRequest`: Username, password, email, and full name validation

### MdmService Models
- `User`: User ID, phone number pattern, tenant validation
- Controllers use `@Valid` annotation for automatic validation

## Error Codes

### Gateway Service
- `AUTHENTICATION_FAILED`
- `ACCESS_DENIED`
- `RESPONSE_STATUS_ERROR`
- `BAD_REQUEST`
- `RUNTIME_ERROR`
- `INTERNAL_ERROR`

### AuthenService
- `AUTHENTICATION_FAILED`
- `INVALID_CREDENTIALS`
- `ACCESS_DENIED`
- `USER_ALREADY_EXISTS`
- `INVALID_TOKEN`
- `VALIDATION_FAILED`
- `INVALID_ARGUMENT`
- `RUNTIME_ERROR`
- `INTERNAL_ERROR`

### MdmService
- `AUTHENTICATION_FAILED`
- `ACCESS_DENIED`
- `USER_NOT_FOUND`
- `USER_ALREADY_EXISTS`
- `INVALID_USER_DATA`
- `VALIDATION_FAILED`
- `INVALID_ARGUMENT`
- `RUNTIME_ERROR`
- `INTERNAL_ERROR`

## Logging

All exception handlers include comprehensive logging:
- Error level logging for all exceptions
- Stack traces for debugging
- Request context information

## Testing Exception Handling

### Test Invalid Authentication
```bash
curl -X GET http://localhost:8080/api/mdm/users
# Expected: 401 with AUTHENTICATION_FAILED
```

### Test Validation Errors
```bash
curl -X POST http://localhost:8082/users \
  -H "Content-Type: application/json" \
  -d '{"userId": "", "phone": "invalid"}'
# Expected: 400 with validation details
```

### Test User Not Found
```bash
curl -X GET http://localhost:8082/users/nonexistent
# Expected: 404 with USER_NOT_FOUND
```

## Benefits

1. **Consistency**: All services return the same error format
2. **Debugging**: Comprehensive logging and error details
3. **User Experience**: Clear, actionable error messages
4. **Monitoring**: Structured error codes for alerting
5. **Security**: No sensitive information leaked in error responses
6. **Maintainability**: Centralized error handling logic

## Dependencies Added

### MdmService
- `spring-boot-starter-validation` for Jakarta validation support

All other required dependencies were already present in the services.

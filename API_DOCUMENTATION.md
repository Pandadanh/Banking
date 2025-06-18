# Banking Authentication System - API Documentation

## 📋 Table of Contents

1. [Authentication Service APIs](#authentication-service-apis)
2. [MDM Service APIs](#mdm-service-apis)
3. [Gateway APIs](#gateway-apis)
4. [Error Response Format](#error-response-format)
5. [Status Codes](#status-codes)

---

## 🔐 Authentication Service APIs

### Base URL: `http://localhost:8081/api/auth`

### 1. Health Check

**Endpoint:** `GET /health`

**Description:** Check service health and statistics

**Request:**
```http
GET /api/auth/health
Content-Type: application/json
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "AUTH-SERVICE",
  "timestamp": "2025-06-17T17:36:41.931Z",
  "stats": {
    "totalUsers": 2,
    "totalTransactions": 7,
    "activeTokens": 2
  }
}
```

---

### 2. User Registration

**Endpoint:** `POST /register`

**Description:** Register a new user account

**Request:**
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser123",
  "password": "password123",
  "email": "testuser123@example.com",
  "fullName": "Test User 123"
}
```

**Request Body Schema:**
```json
{
  "username": "string (required, 3-50 characters, alphanumeric)",
  "password": "string (required, min 8 characters)",
  "email": "string (required, valid email format)",
  "fullName": "string (required, 2-100 characters)"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature"
}
```

**Error Response (409 Conflict):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 409,
  "error": "Conflict",
  "errorCode": "USER_ALREADY_EXISTS",
  "message": "Username 'testuser123' or email is already taken",
  "path": "/api/auth/register",
  "method": "POST",
  "service": "AUTH-SERVICE"
}
```

---

### 3. User Login

**Endpoint:** `POST /login`

**Description:** Authenticate user and return JWT token (with token caching)

**Request:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser123",
  "password": "password123"
}
```

**Request Body Schema:**
```json
{
  "username": "string (required)",
  "password": "string (required)"
}
```

**Response (200 OK) - New Token:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature"
}
```

**Response (200 OK) - Cached Token:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 401,
  "error": "Unauthorized",
  "errorCode": "AUTHENTICATION_FAILED",
  "message": "Invalid username or password",
  "path": "/api/auth/login",
  "method": "POST",
  "service": "AUTH-SERVICE"
}
```

**Error Response (423 Locked):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 423,
  "error": "Locked",
  "errorCode": "ACCOUNT_LOCKED",
  "message": "Account is locked due to multiple failed attempts",
  "path": "/api/auth/login",
  "method": "POST",
  "service": "AUTH-SERVICE"
}
```

---

### 4. Token Validation

**Endpoint:** `POST /validate`

**Description:** Validate JWT token

**Request:**
```http
POST /api/auth/validate
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Response (200 OK) - Valid Token:**
```json
true
```

**Response (200 OK) - Invalid Token:**
```json
false
```

---

### 5. Token Refresh

**Endpoint:** `POST /refresh`

**Description:** Refresh access token using refresh token

**Request:**
```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsInR5cGUiOiJyZWZyZXNoIiwiaWF0IjoxNzE4NjA0MDAwLCJleHAiOjE3MTkyMDg4MDB9.signature"
}
```

**Request Body Schema:**
```json
{
  "refreshToken": "string (required, valid JWT refresh token)"
}
```

**Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNzYwMCwiZXhwIjoxNzE4Njk0MDAwfQ.signature"
}
```

**Error Response (401 Unauthorized):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 401,
  "error": "Unauthorized",
  "errorCode": "INVALID_REFRESH_TOKEN",
  "message": "Refresh token is invalid or expired",
  "path": "/api/auth/refresh",
  "method": "POST",
  "service": "AUTH-SERVICE"
}
```

---

### 6. User Logout

**Endpoint:** `POST /logout`

**Description:** Invalidate user token and logout

**Request:**
```http
POST /api/auth/logout
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Response (200 OK):**
```
(Empty response body)
```

---

## 👥 MDM Service APIs

### Base URL: `http://localhost:8082`

### 1. Health Check

**Endpoint:** `GET /health`

**Description:** Check MDM service health

**Request:**
```http
GET /health
Content-Type: application/json
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "service": "MDM-SERVICE",
  "timestamp": "2025-06-17T17:36:41.931Z"
}
```

---

### 2. Get All Users

**Endpoint:** `GET /users`

**Description:** Retrieve all users (requires authentication)

**Request:**
```http
GET /users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Response (200 OK):**
```json
[
  {
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "phone": "0123456789",
    "tenantId": "tenant-123e4567-e89b-12d3-a456-426614174000",
    "tenantCode": "TENANT01",
    "createdAt": "2025-06-17T17:36:41.931Z",
    "updatedAt": "2025-06-17T17:36:41.931Z"
  },
  {
    "userId": "456e7890-e89b-12d3-a456-426614174001",
    "phone": "0987654321",
    "tenantId": "tenant-456e7890-e89b-12d3-a456-426614174001",
    "tenantCode": "TENANT02",
    "createdAt": "2025-06-17T17:36:41.931Z",
    "updatedAt": "2025-06-17T17:36:41.931Z"
  }
]
```

---

### 3. Get User by ID

**Endpoint:** `GET /users/{id}`

**Description:** Retrieve specific user by ID

**Request:**
```http
GET /users/123e4567-e89b-12d3-a456-426614174000
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Response (200 OK):**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "phone": "0123456789",
  "tenantId": "tenant-123e4567-e89b-12d3-a456-426614174000",
  "tenantCode": "TENANT01",
  "createdAt": "2025-06-17T17:36:41.931Z",
  "updatedAt": "2025-06-17T17:36:41.931Z"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 404,
  "error": "Not Found",
  "errorCode": "USER_NOT_FOUND",
  "message": "User with ID '123e4567-e89b-12d3-a456-426614174000' not found",
  "path": "/users/123e4567-e89b-12d3-a456-426614174000",
  "method": "GET",
  "service": "MDM-SERVICE"
}
```

---

### 4. Create User

**Endpoint:** `POST /users`

**Description:** Create a new user profile

**Request:**
```http
POST /users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json

{
  "userId": "789e0123-e89b-12d3-a456-426614174002",
  "phone": "0111222333",
  "tenantId": "tenant-789e0123-e89b-12d3-a456-426614174002",
  "tenantCode": "TENANT03"
}
```

**Request Body Schema:**
```json
{
  "userId": "string (required, valid UUID)",
  "phone": "string (required, 10-15 digits)",
  "tenantId": "string (required, valid UUID)",
  "tenantCode": "string (required, 2-20 characters, alphanumeric)"
}
```

**Response (201 Created):**
```json
{
  "userId": "789e0123-e89b-12d3-a456-426614174002",
  "phone": "0111222333",
  "tenantId": "tenant-789e0123-e89b-12d3-a456-426614174002",
  "tenantCode": "TENANT03",
  "createdAt": "2025-06-17T17:36:41.931Z",
  "updatedAt": "2025-06-17T17:36:41.931Z"
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 400,
  "error": "Bad Request",
  "errorCode": "VALIDATION_FAILED",
  "message": "Validation failed",
  "path": "/users",
  "method": "POST",
  "service": "MDM-SERVICE",
  "validationErrors": [
    {
      "field": "phone",
      "message": "Phone number must be between 10 and 15 digits"
    },
    {
      "field": "userId",
      "message": "User ID must be a valid UUID"
    }
  ]
}
```

---

### 5. Update User

**Endpoint:** `PUT /users/{id}`

**Description:** Update existing user profile

**Request:**
```http
PUT /users/123e4567-e89b-12d3-a456-426614174000
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json

{
  "phone": "0999888777",
  "tenantCode": "TENANT01_UPDATED"
}
```

**Response (200 OK):**
```json
{
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "phone": "0999888777",
  "tenantId": "tenant-123e4567-e89b-12d3-a456-426614174000",
  "tenantCode": "TENANT01_UPDATED",
  "createdAt": "2025-06-17T17:36:41.931Z",
  "updatedAt": "2025-06-17T18:00:00.000Z"
}
```

---

### 6. Delete User

**Endpoint:** `DELETE /users/{id}`

**Description:** Delete user profile

**Request:**
```http
DELETE /users/123e4567-e89b-12d3-a456-426614174000
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Response (204 No Content):**
```
(Empty response body)
```

---

## 🌐 Gateway APIs

### Base URL: `http://localhost:8080`

### 1. Gateway Health Check

**Endpoint:** `GET /actuator/health`

**Description:** Check gateway service health

**Request:**
```http
GET /actuator/health
Content-Type: application/json
```

**Response (200 OK):**
```json
{
  "status": "UP",
  "components": {
    "discoveryComposite": {
      "status": "UP",
      "components": {
        "eureka": {
          "status": "UP",
          "details": {
            "applications": {
              "AUTH-SERVICE": 1,
              "MDM-SERVICE": 1
            }
          }
        }
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---

### 2. Proxied Authentication APIs

**Base URL:** `http://localhost:8080/api/auth`

All authentication APIs are proxied through the gateway with the same request/response format as the AuthenService, but with additional security headers.

**Example - Login via Gateway:**
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser123",
  "password": "password123"
}
```

**Response includes additional headers:**
```http
HTTP/1.1 200 OK
X-Gateway-Service: GATEWAY
X-Forwarded-To: AUTH-SERVICE
X-Request-ID: 550e8400-e29b-41d4-a716-446655440000
Content-Type: application/json

{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature"
}
```

---

### 3. Proxied MDM APIs

**Base URL:** `http://localhost:8080/users`

All MDM APIs are proxied through the gateway with JWT validation and internal token injection.

**Example - Get Users via Gateway:**
```http
GET /users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
Content-Type: application/json
```

**Internal Request (Gateway → MDM):**
```http
GET /users
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0dXNlcjEyMyIsInVzZXJJZCI6IjEyM2U0NTY3LWU4OWItMTJkMy1hNDU2LTQyNjYxNDE3NDAwMCIsImVtYWlsIjoidGVzdHVzZXIxMjNAZXhhbXBsZS5jb20iLCJuYW1lIjoiVGVzdCBVc2VyIDEyMyIsImlhdCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjkwNDAwfQ.signature
X-Internal-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzZXJ2aWNlIjoiR0FURVdBWSIsInRpbWVzdGFtcCI6MTcxODYwNDAwMCwiZXhwIjoxNzE4NjA0MzAwfQ.internal_signature
X-User-Context: {"userId":"123e4567-e89b-12d3-a456-426614174000","username":"testuser123","email":"testuser123@example.com"}
Content-Type: application/json
```

---

## 🚨 Error Response Format

All services follow a consistent error response format:

### Standard Error Response
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 400,
  "error": "Bad Request",
  "errorCode": "VALIDATION_FAILED",
  "message": "Detailed error message",
  "path": "/api/endpoint",
  "method": "POST",
  "service": "SERVICE-NAME"
}
```

### Validation Error Response
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 400,
  "error": "Bad Request",
  "errorCode": "VALIDATION_FAILED",
  "message": "Validation failed",
  "path": "/api/endpoint",
  "method": "POST",
  "service": "SERVICE-NAME",
  "validationErrors": [
    {
      "field": "fieldName",
      "message": "Field validation error message"
    }
  ]
}
```

### Authentication Error Response
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 401,
  "error": "Unauthorized",
  "errorCode": "AUTHENTICATION_FAILED",
  "message": "Authentication failed",
  "path": "/api/endpoint",
  "method": "POST",
  "service": "SERVICE-NAME"
}
```

---

## 📊 Status Codes

| Status Code | Description | Usage |
|-------------|-------------|-------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST requests |
| 204 | No Content | Successful DELETE requests |
| 400 | Bad Request | Validation errors, malformed requests |
| 401 | Unauthorized | Authentication failed, invalid token |
| 403 | Forbidden | Access denied, insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists, business rule violation |
| 423 | Locked | Account locked due to failed attempts |
| 500 | Internal Server Error | Unexpected server errors |
| 503 | Service Unavailable | Service temporarily unavailable |

---

## 🔑 JWT Token Structure

### Access Token Payload
```json
{
  "sub": "testuser123",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "email": "testuser123@example.com",
  "name": "Test User 123",
  "tenantId": "tenant-123e4567-e89b-12d3-a456-426614174000",
  "iat": 1718604000,
  "exp": 1718690400
}
```

### Refresh Token Payload
```json
{
  "sub": "testuser123",
  "userId": "123e4567-e89b-12d3-a456-426614174000",
  "type": "refresh",
  "iat": 1718604000,
  "exp": 1719208800
}
```

### Internal Token Payload (Gateway → Services)
```json
{
  "service": "GATEWAY",
  "timestamp": 1718604000,
  "exp": 1718604300
}
```

---

## 📝 Notes

1. **Token Caching**: Login API checks for existing valid tokens before generating new ones
2. **Security Headers**: Gateway adds internal tokens and user context for downstream services
3. **Error Consistency**: All services return errors in the same format
4. **Validation**: Comprehensive input validation with detailed error messages
5. **Authentication**: JWT tokens required for all protected endpoints
6. **Logging**: All authentication attempts are logged to LoginTransactions table
7. **Rate Limiting**: Gateway implements rate limiting for security
8. **CORS**: Proper CORS headers for cross-origin requests

---

**🎯 This documentation covers all API endpoints with complete request/response examples for the Banking Authentication System.**

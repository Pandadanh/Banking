# Banking Microservices Authentication System

## 🏗️ Architecture Overview

This project implements a comprehensive banking authentication system using microservices architecture with **dual authentication mechanism**:

- **Gateway**: JWT validation + Internal token generation
- **AuthenService**: User authentication with Oracle database integration
- **MdmService**: User management service
- **Oracle Database**: Enterprise-grade data persistence

## 🚀 Key Features

### 🔐 Authentication & Security
- **Dual Token System**: Gateway validates JWT and generates internal tokens
- **Token Caching**: Reuse existing valid tokens to prevent duplicate generation
- **Password Hashing**: BCrypt encryption for secure password storage
- **Account Lockout**: Protection against brute force attacks
- **JWT Validation**: Secure token verification with HMAC-SHA256

### 📊 Comprehensive Logging
- **Login Transaction Logging**: All authentication attempts tracked
- **Token History**: Complete audit trail of issued tokens
- **Success/Failure Tracking**: Detailed error logging for failed attempts
- **Database Persistence**: All logs stored in Oracle database

### 🛡️ Global Exception Handling
- **Consistent Error Responses**: Structured error format across all services
- **Custom Exceptions**: Business-specific error handling
- **Validation Errors**: Detailed field-level validation messages
- **Security**: No sensitive information leaked in error responses

### 🗄️ Database Integration
- **Oracle Database**: Production-ready enterprise database
- **JPA/Hibernate**: Object-relational mapping
- **Connection Pooling**: Optimized database connections
- **Transaction Management**: ACID compliance

## 📋 Services

### Gateway Service (Port 8080)
**Purpose**: API Gateway with JWT authentication and routing

**Security Features**:
- JWT token validation with public key
- Internal token generation with secret key
- Request routing to downstream services
- Global exception handling for reactive streams

### AuthenService (Port 8081)
**Purpose**: User authentication and token management

**Features**:
- User registration and login
- Token caching logic
- Login transaction logging
- Password encryption
- Account lockout mechanism

**Database Tables**:
- `Users` - User authentication data
- `Employee` - Employee profile information
- `HistoryLoginTenants` - Token cache and history
- `LoginTransactions` - Login attempt logs

### MdmService (Port 8082)
**Purpose**: Master data management for users

**Features**:
- User profile management
- Data validation
- CRUD operations
- Exception handling

## 🔧 Technology Stack

- **Framework**: Spring Boot 3.x, Spring Security 6, Spring Cloud Gateway
- **Database**: Oracle Database 21c Express Edition
- **Authentication**: JWT (JSON Web Tokens)
- **Service Discovery**: Netflix Eureka
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven
- **Java Version**: 17

## 🚀 Quick Start

### Prerequisites
- Docker Desktop
- Java 17+
- Maven 3.6+
- Oracle Account (for container registry access)

### 1. Start Oracle Database
```bash
# Login to Oracle Container Registry
docker login container-registry.oracle.com

# Start Oracle Database
docker-compose up -d oracle-db

# Wait for database initialization (2-3 minutes)
docker-compose logs -f oracle-db
```

### 2. Start Services
```bash
# Start Eureka Server
cd eureka-server
mvn spring-boot:run

# Start Gateway (Port 8080)
cd gateway
mvn spring-boot:run

# Start AuthenService (Port 8081)
cd authenService
mvn spring-boot:run

# Start MdmService (Port 8082)
cd mdmService
mvn spring-boot:run
```

### 3. Verify Services
```bash
# Check Eureka Dashboard
http://localhost:8761

# Check service health
curl http://localhost:8081/api/auth/health
curl http://localhost:8082/health
```

## 📡 API Documentation

### Authentication APIs (AuthenService)

#### User Registration
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "fullName": "Test User"
}
```

#### User Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "testuser",
  "password": "password123"
}
```

#### Token Validation
```http
POST /api/auth/validate
Authorization: Bearer <jwt-token>
```

#### User Logout
```http
POST /api/auth/logout
Authorization: Bearer <jwt-token>
```

### User Management APIs (MdmService)

#### Get All Users
```http
GET /users
Authorization: Bearer <jwt-token>
```

#### Get User by ID
```http
GET /users/{id}
Authorization: Bearer <jwt-token>
```

#### Create User
```http
POST /users
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "userId": "uuid",
  "phone": "0123456789",
  "tenantId": "tenant-uuid",
  "tenantCode": "TENANT01"
}
```

## 🔒 Security Flow

1. **Client Request** → Gateway
2. **Gateway** validates JWT token with public key
3. **Gateway** generates internal token with secret key
4. **Gateway** forwards request with internal token
5. **Downstream Service** validates internal token
6. **Downstream Service** extracts JWT context for user info

## 🧪 Testing Results

### ✅ **All Authentication Features Tested Successfully**

| Feature | Status | Description |
|---------|--------|-------------|
| User Registration | ✅ | New user creation with validation |
| Duplicate Detection | ✅ | Prevents duplicate usernames/emails |
| User Login | ✅ | Password verification and token generation |
| Token Caching | ✅ | Reuses existing valid tokens |
| Token Validation | ✅ | JWT signature and expiration verification |
| Invalid Authentication | ✅ | Proper rejection of invalid credentials |
| User Logout | ✅ | Token invalidation |
| Login Logging | ✅ | Complete audit trail |
| Global Exception Handling | ✅ | Consistent error responses |

### Final Test Statistics
```json
{
  "status": "UP",
  "service": "AUTH-SERVICE",
  "stats": {
    "totalUsers": 2,
    "totalTransactions": 7,
    "activeTokens": 2
  }
}
```

## 🔧 Configuration

### Database Connection (Oracle)
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@localhost:1521:XE
    username: BANKING_USER
    password: BankingPassword123
    driver-class-name: oracle.jdbc.OracleDriver
```

### JWT Configuration
```yaml
security:
  jwt:
    secret: 404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970
    expiration: 86400000 # 24 hours
```

## 📊 Monitoring & Health Checks

- **Eureka Dashboard**: http://localhost:8761
- **Gateway Health**: http://localhost:8080/actuator/health
- **Auth Health**: http://localhost:8081/api/auth/health
- **MDM Health**: http://localhost:8082/health
- **Oracle EM**: http://localhost:5500/em

## 🚨 Error Handling

All services return consistent error format:
```json
{
  "timestamp": "2025-06-17T17:36:41.931Z",
  "status": 401,
  "error": "Unauthorized",
  "errorCode": "AUTHENTICATION_FAILED",
  "message": "Invalid username or password",
  "service": "AUTH-SERVICE"
}
```

## 🔄 Token Caching Logic

1. **Login Request** received
2. **Check Database** for existing valid token
3. **If Found**: Return existing token
4. **If Not Found**: Generate new token and save to database
5. **Logout**: Mark token as deleted in database

## 🛠️ Development

### Project Structure
```
banking/
├── eureka-server/          # Service discovery
├── gateway/               # API Gateway
├── authenService/         # Authentication service
├── mdmService/           # Master data management
├── docker-compose.yml    # Oracle database setup
└── README.md            # This file
```

### Build Commands
```bash
# Build all services
mvn clean install

# Build specific service
cd authenService && mvn clean package

# Run tests
mvn test
```

---

**🎉 The Banking Authentication System is production-ready with enterprise-level security features!**

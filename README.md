# Spring Boot JWT Authentication

A complete implementation of JWT (JSON Web Token) authentication and authorization in Spring Boot 3. This project serves as a reference template for implementing secure JWT-based authentication in Spring Boot applications.

## 📚 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [How It Works](#how-it-works)
- [Testing](#testing)
- [Security Configuration](#security-configuration)

## Overview

This project demonstrates how to implement JWT-based authentication in a Spring Boot application. It includes user authentication, token generation, token validation, and role-based authorization with Spring Security.

## ✨ Features

- 🔐 **JWT Authentication** - Secure token-based authentication
- 👥 **User Management** - Pre-configured users with different roles
- 🛡️ **Spring Security** - Complete security configuration
- 📊 **H2 Database** - In-memory database for testing
- 🔑 **Role-Based Access** - USER and ADMIN roles
- ⚙️ **Custom Security Filters** - JWT validation on each request
- 📦 **Lombok Integration** - Reduced boilerplate code
- 📝 **H2 Console** - Database management interface

## 🛠️ Technologies Used

- **Spring Boot** 3.5.7
- **Java** 21
- **Spring Security**
- **Spring Data JPA**
- **H2 Database** (In-memory)
- **Lombok**
- **Maven**
- **JJWT** (JSON Web Token library)

## 📁 Project Structure

```
jwt/
├── src/
│   ├── main/
│   │   ├── java/com/security/jwt/
│   │   │   ├── jwtsec/
│   │   │   │   ├── AuthEntryPointJwt.java       # Handles authentication errors
│   │   │   │   ├── LoginRequest.java            # Login request DTO
│   │   │   │   ├── LoginResponse.java           # Login response DTO
│   │   │   │   ├── SecurityConfig.java          # Security configuration
│   │   │   │   ├── jwtUtil.java                 # JWT utility methods
│   │   │   │   └── jwt_application_filter.java  # JWT filter for requests
│   │   │   ├── Controller.java              # REST API endpoints
│   │   │   └── JwtApplication.java          # Main application class
│   │   └── resources/
│   │       ├── application.properties       # Application configuration
│   │       └── schema.sql                   # Database schema
│   └── test/
└── pom.xml
```

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/Sufyan786-ui/Spring_jwt.git
cd Spring_jwt/jwt
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console

Visit `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (leave empty)

## ⚙️ Configuration

### application.properties

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=none
```

### Default Users

The application comes with two pre-configured users:

| Username | Password | Role  |
|----------|----------|-------|
| user     | password | USER  |
| admin    | password | ADMIN |

## 📡 API Endpoints

### Public Endpoints

#### Login
```http
POST /login
Content-Type: application/json

{
  "username": "user",
  "password": "password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "user"
}
```

### Protected Endpoints

#### Get Hello Message
```http
GET /hello
Authorization: Bearer <your-jwt-token>
```

**Response:**
```
Hello World
```

## 🔍 How It Works

### Authentication Flow

1. **User Login**
   - User sends credentials (username/password) to `/login`
   - Spring Security authenticates the user
   - If valid, JWT token is generated and returned

2. **Token Generation**
   - `jwtUtil` class generates a JWT token
   - Token contains user information and expiration time
   - Token is signed with a secret key

3. **Accessing Protected Resources**
   - Client includes JWT token in Authorization header
   - `jwt_application_filter` intercepts the request
   - Filter validates the token
   - If valid, user is authenticated and request proceeds

4. **Token Validation**
   - Extract token from Authorization header
   - Verify token signature
   - Check token expiration
   - Extract user details from token

### Security Components

**SecurityConfig.java**
- Configures HTTP security
- Defines public and protected endpoints
- Sets up JWT filter in the security chain
- Configures user details service
- Disables CSRF (for stateless JWT auth)

**jwt_application_filter.java**
- Extends `OncePerRequestFilter`
- Intercepts every HTTP request
- Validates JWT token
- Sets authentication in Security Context

**jwtUtil.java**
- Generates JWT tokens
- Validates tokens
- Extracts information from tokens

**AuthEntryPointJwt.java**
- Handles authentication errors
- Returns 401 Unauthorized for invalid/missing tokens

## 🧪 Testing

### Using Postman or cURL

1. **Login to get token**
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}'
```

2. **Use token to access protected endpoint**
```bash
curl -X GET http://localhost:8080/hello \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Example Test Scenario

```bash
# 1. Login as user
TOKEN=$(curl -s -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"password"}' \
  | jq -r '.token')

# 2. Access protected endpoint
curl -X GET http://localhost:8080/hello \
  -H "Authorization: Bearer $TOKEN"
```

## 🛡️ Security Configuration

### Key Security Features

- **Stateless Session Management** - No server-side sessions
- **CSRF Disabled** - Not needed for stateless JWT
- **CORS Configured** - Cross-origin requests handled
- **Password Encoding** - BCrypt password encoder
- **Role-Based Access** - Different permissions for USER and ADMIN

### Security Best Practices

- ✅ Store JWT secret key securely (use environment variables)
- ✅ Set appropriate token expiration time
- ✅ Use HTTPS in production
- ✅ Validate tokens on every request
- ✅ Handle token expiration gracefully
- ✅ Implement refresh token mechanism for production

## 📝 Notes

- This is a reference implementation for learning purposes
- H2 database is in-memory and resets on restart
- For production, use a persistent database (PostgreSQL, MySQL)
- Add refresh token mechanism for better UX
- Implement token blacklisting for logout functionality
- Add proper exception handling and validation

## 🔗 Useful Resources

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/) - JWT Debugger
- [Spring Boot Reference](https://spring.io/projects/spring-boot)

## 👤 Author

**Sufyan**
- GitHub: [@Sufyan786-ui](https://github.com/Sufyan786-ui)

## 📝 License

This project is open source and available for learning purposes.

---

**Note:** This project serves as a quick reference for implementing JWT authentication in Spring Boot applications. Feel free to use it as a template for your own projects!

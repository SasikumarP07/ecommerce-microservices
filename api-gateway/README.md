# API Gateway - E-commerce Microservices Project

This is the **API Gateway** component of the E-commerce Microservices Application. It is built using **Spring Cloud Gateway** and acts as a single entry point for routing requests to downstream services like Auth, Product, Order, Cart, etc.

---

## Features

- Route requests to different microservices
- Apply security filters (JWT authentication & validation)
- Centralized request logging
- Custom error handling (optional)
- Load balancing with Eureka
- Configurable routes

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Cloud Gateway
- Spring Security
- Spring WebFlux
- Eureka Client
- JWT Token Validation
- Maven

---

## JWT Authentication Flow

1. Client sends a request with JWT token in `Authorization` header.
2. Gateway intercepts it using `JwtValidationFilter`.
3. Token is validated using `JwtUtil` (from `common_util` service).
4. If valid, the request proceeds to the appropriate service.
5. If invalid or missing, a `401 Unauthorized` is returned.

---

## Test Endpoints

To test from Postman or Web browser:

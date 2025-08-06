# 🛠️ common_util - Utility Module for E-commerce Microservices

This is the **common_util** module of the E-commerce Microservices Application. It provides shared utility classes such as JWT token utilities and request filters that are reused across all microservices, ensuring consistency and reducing duplication.

---

## 📦 Features

✅ `JwtUtil` for JWT token generation and validation  
✅ `JwtValidationFilter` for intercepting and validating JWTs in incoming requests  
✅ Can be integrated with any service using Spring Security  
✅ Centralized security logic for consistent token verification  
✅ Reduces redundancy by sharing utility code across services

---

## 🧱 Technologies Used

Java 17  
Spring Boot  
Spring Security  
JWT (io.jsonwebtoken library)  
Maven

---

## 🔐 JWT Handling Flow

1. Microservice receives an HTTP request.
2. `JwtValidationFilter` intercepts the request and extracts the JWT token from the `Authorization` header.
3. `JwtUtil` validates the token's signature and expiration.
4. If the token is valid:
    - The user's authentication is set in the security context.
    - Request proceeds to the controller.
5. If invalid, the filter responds with `401 Unauthorized`.

---

## 📁 Included Components

### 🔧 JwtUtil

A utility class for:
- Generating JWT tokens with custom claims
- Validating token signature and expiration
- Extracting username and roles from tokens

### 🛡️ JwtValidationFilter

A Spring Security filter that:
- Intercepts requests before they reach secured endpoints
- Extracts and validates JWT using `JwtUtil`
- Authenticates the user in the Spring Security context

---

## 🔌 Integration Guide

1. **Add `common_util` as a dependency** in other microservices (via internal Maven repo or local module).

2. **Register `JwtValidationFilter`** in the security configuration:
   ```java
   http.addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class);

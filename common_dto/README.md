# 🧰 common_util Module – Shared Utilities for E-commerce Microservices

The **common_util** module provides shared utility components across all E-commerce microservices, including JWT handling, filters, and other reusable logic. It centralizes cross-cutting concerns to avoid duplication and ensure consistency.

---

## 📦 Features

✅ **JWT Token Generation** (`JwtUtil.generateToken`)  
✅ **JWT Validation** (`JwtUtil.validateToken`)  
✅ **Claim Extraction** (`JwtUtil.extractUsername`, `extractUserRole`, `extractClaim`)  
✅ **Signing Key Management** (Base64-encoded secret → HMAC-SHA256)  
✅ **Once-Per-Request JWT Filter** (`JwtFilter`) for Spring Security  
✅ **Comprehensive Logging** for tracing token lifecycle and errors

---

## 🧱 Technologies Used

- Java 17
- Spring Boot
- Spring Security
- jjwt (io.jsonwebtoken)
- SLF4J / Logback
- Jakarta Servlet API
- Lombok
- Maven

---

## 🔐 JWT Configuration

application.properties

jwt:
  secret: ${BASE64_ENCODED_256_BIT_SECRET}


👤 User Service - E-commerce Microservices Project
The User Service is a core microservice responsible for managing user profiles and addresses within the E-commerce system. It works closely with the Auth Service (for authentication), Order Service, and other services to provide a complete user experience.

----

📦 Features
🧾 CRUD operations for user profiles

📍 Manage multiple addresses per user

🔐 Integrates with Spring Security (via CustomUserDetailsService)

📧 Email-based user lookup

🔄 DTO-based clean service interaction

----

🏗️ Tech Stack
Java 17

Spring Boot

Spring Data JPA

Spring Security

Lombok

H2 / MySQL

Maven

----


🔐 Security Integration
The service implements UserDetailsService using CustomUserDetailsService to provide user details to Spring Security (used in the Auth Service). It loads users by their email and provides their role as a SimpleGrantedAuthority.

---

🧪 Testing
Unit tests can be written for services using JUnit & Mockito.

Integration tests can be written using Spring Boot Test.

Ensure the @Transactional and proper @MockBean setup in tests.

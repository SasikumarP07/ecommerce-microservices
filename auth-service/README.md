# Auth Service - E-commerce Microservices Project

This is the **Auth Service** component of the E-commerce Microservices Application.  
It handles user registration, authentication, JWT token generation, and integrates with User and Notification services.

---

## Features

- User registration with secure password hashing
- User login with JWT token generation
- Email existence verification
- Integration with User Service for profile creation
- Integration with Notification Service for sending notifications
- Exception handling for invalid login attempts
- Unit tested with Mockito and JUnit 5

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Mockito & JUnit 5
- Maven

---

## Authentication Flow

1. Client sends registration or login request.
2. Passwords are securely hashed using `PasswordEncoder`.
3. On successful login, JWT token is generated using `JwtUtil`.
4. Token is returned to client for use in subsequent requests.
5. User profile is created via User Service client.
6. Welcome notification sent via Notification Service client.  

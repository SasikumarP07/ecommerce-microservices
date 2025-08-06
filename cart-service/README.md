# 🛡️ Auth Service - E-commerce Microservices Project

The **Auth Service** is a secure authentication module within the E-commerce Microservices Application. It manages user sign-up, login, and JWT-based authentication, and interacts with the **User Service** and **Notification Service**.

---

## 📦 Features

✅ User registration with secure password hashing  
✅ User login with JWT token generation  
✅ Email existence verification  
✅ Integration with **User Service** for profile creation  
✅ Integration with **Notification Service** for sending welcome messages  
✅ Robust exception handling for invalid credentials or duplicates  
✅ Unit tested using **Mockito** and **JUnit 5**

---

## 🧱 Technologies Used

- Java 17
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Cloud OpenFeign
- Eureka Client
- Lombok
- Maven
- JUnit 5 & Mockito

---

## 🔐 Authentication Flow

1. Client sends a sign-up or login request to `/auth/signup` or `/auth/login`.
2. Passwords are securely hashed using `PasswordEncoder`.
3. On successful login, a JWT token is generated via `JwtUtil`.
4. The token is sent back to the client for authenticating future requests.
5. During registration, a profile is created via the **User Service**.
6. A welcome notification is triggered using the **Notification Service**.

---

## 🔗 API Endpoints

Base URL: `/api/auth`

| Method | Endpoint      | Description                      |
|--------|---------------|----------------------------------|
| POST   | `/signup`     | Registers a new user             |
| POST   | `/login`      | Authenticates user & returns JWT |
| GET    | `/check-email`| Verifies email existence         |

---

## 🧪 Testing

- Unit tested using **Mockito** and **JUnit 5**
- Tests cover service logic and edge cases
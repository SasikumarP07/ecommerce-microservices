# User Service - E-commerce Microservices Project

The User Service is a core microservice responsible for managing user profiles and addresses within the E-commerce system.  
It works closely with the Auth Service (for authentication), Order Service, and other services to provide a complete user experience.

---

## Features

- CRUD operations for user profiles
- Manage multiple addresses per user
- Integration with Spring Security (via `CustomUserDetailsService`)
- Email-based user lookup
- DTO-based clean service interaction

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Lombok
- H2 / MySQL
- Maven

---

## Security Integration

- Implements `UserDetailsService` via `CustomUserDetailsService`
- Provides user details to Spring Security (consumed by Auth Service)
- Loads users by email and assigns roles as `SimpleGrantedAuthority`

---

## API Endpoints

Base URL: `/api/users`

| Method | Endpoint               | Description                        | Access |
|--------|------------------------|------------------------------------|--------|
| POST   | `/`                    | Create a new user profile          | Public |
| GET    | `/{id}`                | Retrieve user by ID                | User   |
| GET    | `/email/{email}`       | Retrieve user by email             | User   |
| PUT    | `/{id}`                | Update user profile                | User   |
| DELETE | `/{id}`                | Delete user profile                | User   |

### Address APIs

| Method | Endpoint                 | Description                          | Access |
|--------|--------------------------|--------------------------------------|--------|
| POST   | `/{userId}/addresses`    | Add a new address for a user          | User   |
| GET    | `/{userId}/addresses`    | Get all addresses for a user          | User   |
| PUT    | `/{userId}/addresses/{id}` | Update a user’s address by ID       | User   |
| DELETE | `/{userId}/addresses/{id}` | Delete a user’s address by ID       | User   |

---

## Database Design

### Table: users
| Column      | Type        | Description                |
|-------------|-------------|----------------------------|
| id          | BIGINT (PK) | Auto-generated user ID     |
| name        | VARCHAR     | Full name                  |
| email       | VARCHAR     | Unique email (login ID)    |
| password    | VARCHAR     | Encrypted password         |
| role        | VARCHAR     | User role (USER/ADMIN)     |
| created_at  | TIMESTAMP   | Created timestamp          |
| updated_at  | TIMESTAMP   | Last updated timestamp     |

### Table: addresses
| Column      | Type        | Description                   |
|-------------|-------------|-------------------------------|
| id          | BIGINT (PK) | Auto-generated address ID      |
| user_id     | BIGINT (FK) | Linked user ID                |
| street      | VARCHAR     | Street details                |
| city        | VARCHAR     | City                          |
| state       | VARCHAR     | State                         |
| country     | VARCHAR     | Country                       |
| postal_code | VARCHAR     | Postal/Zip code               |

---

## Testing

- Unit tests for services using **JUnit & Mockito**
- Integration tests with **Spring Boot Test**
- Use `@Transactional` for database-related tests
- Use `@MockBean` for mocking dependencies in service-level tests

---

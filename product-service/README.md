# Product Service - E-commerce Microservices Project

The Product Service is a core component of the E-commerce Microservices Architecture.  
It manages all product and category-related operations, including creation, updates, retrieval, filtering, and deletion.  
It also supports advanced features such as pagination, sorting, recommendations, and integration with the Inventory Service.

---

## Features

- Create, update, and delete products
- Manage categories (CRUD operations)
- Filter products by name, category, and price
- Pagination and sorting support
- Get Top 10 latest products
- Get product recommendations per user
- Role-based access control (Admin/User) via JWT
- Inventory sync via Feign Client
- Standardized request/response DTOs (via `common_dto` module)

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security + JWT
- MySQL / PostgreSQL
- Feign Client
- Lombok
- Maven
- Eureka (Service Discovery)
- Spring Cloud Gateway (routing & JWT validation)

---

## Security

- JWT-based authentication
- Admin-only access for create/update/delete endpoints
- Role validation handled at API Gateway via `JwtValidationFilter`
- Internal microservice calls secured via propagated JWT

---

## Integration

- Inventory Service:  
  Automatically creates inventory entry when a new product is added.
- Common DTO/Util Module:  
  Reuses `JwtUtil`, `JwtFilter`, and DTO classes for consistency.

---

## API Endpoints

Base URL: `/api/products`

| Method | Endpoint                  | Description                          | Access   |
|--------|---------------------------|--------------------------------------|----------|
| POST   | `/`                       | Create a new product                 | Admin    |
| PUT    | `/{id}`                   | Update a product by ID               | Admin    |
| DELETE | `/{id}`                   | Delete a product by ID               | Admin    |
| GET    | `/`                       | Get all products (pagination + sort) | Public   |
| GET    | `/latest`                 | Get Top 10 latest products           | Public   |
| GET    | `/suggested/{userId}`     | Get suggested products for user      | User     |
| GET    | `/filter`                 | Filter products by name/category/price | Public |
| GET    | `/{id}`                   | Get product by ID                    | Public   |

### Category APIs

| Method | Endpoint         | Description                  | Access |
|--------|------------------|------------------------------|--------|
| POST   | `/categories`    | Create a new category        | Admin  |
| GET    | `/categories`    | Get all categories           | Public |
| GET    | `/categories/{id}` | Get category by ID         | Public |
| PUT    | `/categories/{id}` | Update category by ID      | Admin  |
| DELETE | `/categories/{id}` | Delete category by ID      | Admin  |

---

## Database Design

### Table: products
| Column        | Type        | Description                          |
|---------------|-------------|--------------------------------------|
| id            | BIGINT (PK) | Auto-generated product ID            |
| name          | VARCHAR     | Product name                         |
| description   | TEXT        | Product description                  |
| price         | DECIMAL     | Product price                        |
| category_id   | BIGINT (FK) | Linked category ID                   |
| created_at    | TIMESTAMP   | Created timestamp                    |
| updated_at    | TIMESTAMP   | Last updated timestamp               |

### Table: categories
| Column        | Type        | Description                          |
|---------------|-------------|--------------------------------------|
| id            | BIGINT (PK) | Auto-generated category ID           |
| name          | VARCHAR     | Category name                        |
| description   | VARCHAR     | Category description                 |

---

## Testing

- Unit and Integration tests (WIP)
- Exception handling tested with custom exceptions:
    - ResourceNotFoundException
    - UnauthorizedAccessException
- Mockito used for service-level testing
- Spring Boot Test for integration tests

---

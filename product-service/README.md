🛍️ Product Service - E-commerce Microservices Project
The Product Service is a core component of the E-commerce Microservices Architecture. It handles all operations related to products and categories, such as creation, update, retrieval, filtering, and deletion. It also supports pagination, sorting, and recommendation features.

----

🚀 Features
✅ Create, update, and delete products
✅ Category management (CRUD)
✅ Product filtering by name, category, and price
✅ Sorting and pagination support
✅ Top 10 latest products
✅ Suggested products by user
✅ Role-based access control using JWT
✅ Integration with Inventory Service via Feign Client
✅ Standardized request/response DTOs via common_dto module

----

🏗️ Tech Stack
Java 17

Spring Boot

Spring Data JPA

Spring Security + JWT

MySQL/PostgreSQL

Feign Client

Lombok

Maven

Eureka (Service Discovery)

Gateway (for routing & JWT validation)

----

🔐 Security
JWT-based authentication

Admin-only access for create/update/delete

Role validation is handled in the API Gateway using JwtValidationFilter

Internal service calls secured via propagated JWT

---

🔁 Integration
Inventory Service: Updates inventory automatically when a product is created.

Common Util Module: Reuses JwtUtil, JwtFilter, and DTO classes.

----

🧪 Testing
Unit and integration tests (WIP)

Exception handling tested using custom exceptions like ResourceNotFoundException, UnauthorizedAccessException



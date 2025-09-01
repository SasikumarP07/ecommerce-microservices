# Inventory Service - E-commerce Microservices Project

The **Inventory Service** is a core component of the E-commerce Microservices Architecture. It is responsible for managing product inventory, including stock creation, retrieval, updates, and reductions. It ensures real-time synchronization of stock across the platform.

---

## Features

- Create inventory for new products
- Retrieve stock quantity by product ID
- Update inventory stock
- Reduce stock quantity during order placement
- Custom exception handling
- DTO-based communication
- Integration with Product and Order services (via Feign client or REST)

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Spring Cloud Netflix Eureka (Service Discovery)
- Lombok
- Feign Client (for inter-service communication)
- Maven
- SLF4J (Logging)

---

## API Endpoints

Base URL: `/api/inventory`

| Method | Endpoint                  | Description                       |
|--------|---------------------------|-----------------------------------|
| POST   | `/`                       | Create inventory for a product    |
| GET    | `/{productId}`            | Get stock quantity by product ID  |
| PUT    | `/{productId}`            | Update inventory stock            |
| PUT    | `/{productId}/reduce`     | Reduce stock during order         |

---

## How It Works

1. The service stores and manages stock levels in the database.
2. When a product is created in the **Product Service**, the **Inventory Service** initializes stock.
3. During order placement, stock is reduced atomically to ensure consistency.
4. Inter-service communication happens via Feign clients or REST APIs.

---

## Database Design

**Table: inventory**

| Column       | Type        | Description                  |
|--------------|-------------|---------------

# Order Service - E-commerce Microservices Project

The **Order Service** is a core component of the E-commerce Microservices Architecture. It manages all customer order operations, including placing orders, retrieving details, listing user orders, and canceling orders. It interacts with the **Cart Service**, **Product Service**, and **Inventory Service** to ensure proper order processing.

---

## Features

- Place a new order
- Retrieve order by ID
- Retrieve all orders by user ID
- Retrieve all orders (Admin access)
- Cancel an existing order
- DTO ↔ Entity mapping using Mappers
- Logging for all key operations

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Lombok
- MySQL (or any JPA-compatible database)
- Maven
- Feign Client (for inter-service communication)
- Eureka Discovery Client (for service registry)
- Spring Cloud Config (optional)
- JWT (via API Gateway)

---

## API Endpoints

Base URL: `/api/orders`

| Method | Endpoint         | Description                                |
|--------|------------------|--------------------------------------------|
| POST   | `/place`         | Place a new order                          |
| GET    | `/{orderId}`     | Retrieve order details by ID               |
| GET    | `/user/{userId}` | Retrieve all orders for a specific user    |
| GET    | `/`              | Retrieve all orders (admin access only)    |
| PUT    | `/cancel/{id}`   | Cancel an existing order                   |

---

## Order Processing Flow

1. A customer places an order through the **Order Service**.
2. The service fetches cart details from the **Cart Service**.
3. Product information is retrieved from the **Product Service**.
4. Stock availability is validated and reduced in the **Inventory Service**.
5. The order is saved in the database.
6. A notification request is sent to the **Notification Service** (e.g., order confirmation).

---

## Database Design

**Table: orders**

| Column       | Type        | Description                          |
|--------------|-------------|--------------------------------------|
| id           | BIGINT (PK) | Auto-generated ID                    |
| user_id      | BIGINT      | ID of the customer who placed order  |
| status       | VARCHAR     | Order status (PLACED, CANCELLED)     |
| total_price  | DECIMAL     | Total order amount                   |
| created_at   | TIMESTAMP   | Timestamp of order creation          |

---

## Example Request

**Place Order (POST `/api/orders/place`)**
```json
{
  "userId": 1,
  "cartId": 101,
  "paymentMethod": "CREDIT_CARD"
}

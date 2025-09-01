# Payment Service - E-commerce Microservices Project

The **Payment Service** is responsible for handling all payment-related operations in the E-commerce Microservices Application. It manages payment processing, tracks payment status, and integrates with the **Order Service** to validate and update payment outcomes.

---

## Features

- Process payments using multiple methods (Credit Card, Debit Card, UPI, etc.)
- Maintain and retrieve payment status (`PENDING`, `SUCCESS`, `FAILED`)
- Fetch payment details by `orderId`
- Integration-ready with Order Service for order validation
- Exception handling for invalid payment requests
- Clean architecture (DTOs, Service, Repository layers)
- Unit tested with Mockito and JUnit 5

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 / MySQL (for persistence)
- Mockito & JUnit 5
- Maven

---

## API Endpoints

Base URL: `/api/payments`

| Method | Endpoint              | Description                         |
|--------|-----------------------|-------------------------------------|
| POST   | `/process`            | Process a new payment               |
| GET    | `/order/{orderId}`    | Retrieve payment details by orderId |
| GET    | `/{paymentId}`        | Retrieve payment details by paymentId |

---

## Payment Flow

1. Client sends a `PaymentRequestDTO` containing:
    - `orderId`
    - `paymentMethod`
    - `amount`

2. Payment is initialized with status `PENDING`.
3. Service validates order (via Order Service).
4. Status is updated to either `SUCCESS` or `FAILED`.
5. Response returned with:
    - `paymentId`
    - `status`
    - `transactionTime`

---

## Database Design

**Table: payments**

| Column          | Type        | Description                            |
|-----------------|-------------|----------------------------------------|
| id              | BIGINT (PK) | Auto-generated payment ID              |
| order_id        | BIGINT      | Linked order ID                        |
| amount          | DECIMAL     | Payment amount                         |
| method          | VARCHAR     | Payment method (CREDIT, DEBIT, UPI)    |
| status          | VARCHAR     | Payment status (PENDING, SUCCESS, FAIL)|
| transaction_time| TIMESTAMP   | Timestamp of the transaction           |

---

## Example Request

**Process Payment (POST `/api/payments/process`)**
```json
{
  "orderId": 5001,
  "paymentMethod": "CREDIT_CARD",
  "amount": 2500.00
}

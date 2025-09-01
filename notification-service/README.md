# Notification Service - E-commerce Microservices Project

The **Notification Service** is responsible for managing and sending notifications within the E-commerce Microservices Application. It stores and sends notification messages such as order confirmations, user welcome messages, and other system alerts.

---

## Features

- Accepts and stores notification requests
- Sends notification messages (e.g., email or logs)
- Returns a response with notification status
- Integrates seamlessly with other microservices such as Auth Service and Order Service
- Unit tested with JUnit and Mockito

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database (for development and testing)
- Lombok
- Maven
- JUnit 5
- Mockito

---

## API Endpoints

Base URL: `/api/notifications`

| Method | Endpoint | Description                          |
|--------|----------|--------------------------------------|
| POST   | `/send`  | Accepts and sends a new notification |
| GET    | `/`      | Retrieves all stored notifications   |
| GET    | `/{id}`  | Retrieves notification by ID         |

---

## How It Works

1. Other services (e.g., Auth or Order) send a notification request to the Notification Service.
2. The request is saved into the database.
3. The service simulates sending (via email, log, or message broker).
4. A success response with the notification status is returned.

---

## Database Design

**Table: notifications**

| Column       | Type        | Description                  |
|--------------|-------------|------------------------------|
| id           | BIGINT (PK) | Auto-generated ID            |
| recipient    | VARCHAR     | Notification recipient email |
| message      | TEXT        | Notification content         |
| type         | VARCHAR     | Notification type (e.g., WELCOME, ORDER_CONFIRMATION) |
| status       | VARCHAR     | Status of notification       |
| created_at   | TIMESTAMP   | Timestamp of creation        |

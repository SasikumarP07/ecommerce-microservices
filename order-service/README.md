# 📦 Order Service - E-commerce Microservices Project

The **Order Service** is a core component of the E-commerce Microservices Architecture. It handles all operations related to customer orders, including placing orders, retrieving order details, listing orders, and canceling orders. This service communicates with the **Cart Service**, **Product Service**, and **Inventory Service** as part of the end-to-end order processing workflow.

---

## 🚀 Features

- ✅ Place a new order
- ✅ Retrieve order by ID
- ✅ Retrieve all orders by user ID
- ✅ Retrieve all orders (Admin access)
- ✅ Cancel an existing order
- ✅ Convert DTOs to Entities and vice versa using Mappers
- ✅ Logging for all key operations

---

## 🛠️ Tech Stack

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



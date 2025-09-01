# Common DTO - E-commerce Microservices Project

The **Common DTO** module provides reusable **Data Transfer Objects (DTOs)** shared across all microservices in the E-commerce Application.  
This ensures consistency, reduces duplication, and improves maintainability when services communicate with each other.

---

## Features

- Centralized DTO definitions for cross-service communication
- Consistent request/response structures across microservices
- Shared enums and constants
- Reduces boilerplate and duplication in services

---

## Technologies Used

- Java 17
- Lombok (for boilerplate reduction)
- Maven

---

## Structure

The module is organized by domain-specific packages:

- `auth` → DTOs for authentication & user login/registration
- `user` → DTOs for user profiles and user details
- `product` → DTOs for product details, pricing, and inventory
- `order` → DTOs for order requests and responses
- `cart` → DTOs for cart items and cart operations
- `payment` → DTOs for payment requests and responses
- `notification` → DTOs for email/notification payloads

---

## Usage

1. Add the **common-dto** module as a Maven dependency in your microservice `pom.xml`:

```xml
<dependency>
    <groupId>com.ecommerce</groupId>
    <artifactId>common-dto</artifactId>
    <version>1.0.0</version>
</dependency>

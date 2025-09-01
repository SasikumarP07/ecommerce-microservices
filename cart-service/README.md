# Cart Service - E-commerce Microservices Project

The **Cart Service** manages shopping cart operations for the E-commerce Microservices Application.  
It allows users to add, update, and remove items from their cart, and integrates with the **Product Service** to validate product details.

---

## Features

- Add products to a user’s cart
- Update product quantity in the cart
- Remove items from the cart
- Retrieve cart items for a user
- Clear cart after order placement
- Validates product availability and pricing via **Product Service**
- Exception handling for invalid cart operations

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL (or PostgreSQL)
- Spring Cloud OpenFeign
- Eureka Client
- Lombok
- Maven
- JUnit 5 & Mockito

---

## Cart Flow

1. User sends a request to add/update/remove items in their cart.
2. Cart Service validates product details using **Product Service**.
3. Cart items are persisted in the database.
4. When an order is placed, the cart is cleared.

---

## API Endpoints

Base URL: `/api/cart`

| Method | Endpoint             | Description                      |
|--------|----------------------|----------------------------------|
| POST   | `/add`               | Add a product to the cart        |
| PUT    | `/update/{itemId}`   | Update quantity of a cart item   |
| DELETE | `/remove/{itemId}`   | Remove a product from the cart   |
| GET    | `/user/{userId}`     | Get all cart items for a user    |
| DELETE | `/clear/{userId}`    | Clear cart for a specific user   |

---

## Testing

- Unit tested using **Mockito** and **JUnit 5**
- Tests cover service logic, product validation, and edge cases  

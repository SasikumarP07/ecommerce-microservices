# Eureka Service - Service Discovery for E-commerce Microservices

The **Eureka Service** is the central **Service Discovery Server** in the E-commerce Microservices Architecture. It enables all other microservices to register themselves and discover other services without hardcoding their network locations.

---

## Features

- Acts as a registry for all microservices
- Enables dynamic service discovery
- Reduces coupling between services
- Supports load-balanced and failover-aware client communication
- Provides a web dashboard to view registered instances

---

## Technologies Used

- Java 17
- Spring Boot
- Spring Cloud Netflix Eureka
- Maven

---

## How It Works

1. Eureka Server starts and listens for registration requests.
2. Each microservice registers itself with the Eureka Server using its `application.yml`.
3. Eureka maintains a registry of all live services.
4. Microservices can discover each other by service name using Eureka Client with LoadBalancer or Feign.

---

## Getting Started

### Start the Eureka Server

Make sure `@EnableEurekaServer` is added in your main class:

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}

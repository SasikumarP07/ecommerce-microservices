package com.ecommerce.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Entry point for the Auth Service microservice in the e-commerce application.
 *
 * <p>This class bootstraps the Spring Boot application and enables integration with:
 * <ul>
 *     <li>Spring Cloud Discovery Client (Eureka)</li>
 *     <li>OpenFeign for inter-service communication</li>
 * </ul>
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = {
		"com.ecommerce.auth_service",
		"com.ecommerce.common_util.util" // Add this if needed
})
public class AuthServiceApplication {
	/**
	 * Main method that launches the Auth Service application.
	 *
	 * @param args application arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}
}

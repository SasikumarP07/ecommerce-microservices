package com.ecommerce.cart_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main class for the Cart Service application.
 * <p>
 * Annotations used:
 * - @SpringBootApplication: Enables auto-configuration and component scanning.
 * - @EnableDiscoveryClient: Registers this service with the Eureka Discovery Server.
 * - @EnableFeignClients: Enables Feign clients to communicate with other microservices.
 * - @EnableAsync: Enables asynchronous processing (used for async product fetching).
 */
@EnableDiscoveryClient
@EnableFeignClients
@EnableAsync
@SpringBootApplication(scanBasePackages = {
		"com.ecommerce.auth_service",
		"com.ecommerce.common_util.util"
})
public class CartServiceApplication {

	/**
	 * The main method that serves as the entry point for the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CartServiceApplication.class, args);
	}

}

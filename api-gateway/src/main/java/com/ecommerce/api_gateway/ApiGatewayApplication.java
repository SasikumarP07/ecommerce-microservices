package com.ecommerce.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Entry point for the API Gateway microservice.
 * This application acts as the centralized entry point for all incoming client requests.
 * It routes requests to appropriate microservices using Spring Cloud Gateway
 * and registers itself with the Eureka Discovery Server.
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {
		"com.ecommerce.api_gateway",
		"com.ecommerce.common_util.util" // Add this if needed
})

public class ApiGatewayApplication {

	/**
	 * Main method that launches the API Gateway application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		System.out.println("Gateway App Starting...");
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}

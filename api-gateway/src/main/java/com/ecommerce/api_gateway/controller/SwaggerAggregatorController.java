package com.ecommerce.api_gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/swagger-aggregator")
@Tag(name = "Swagger Aggregator", description = "API for aggregating Swagger documentation from microservices")
public class SwaggerAggregatorController {

    private final WebClient webClient;

    public SwaggerAggregatorController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Operation(
            summary = "Get OpenAPI documentation for a specific microservice",
            description = "Fetches the Swagger/OpenAPI JSON for the specified service like user, product, or order"
    )
    @GetMapping("/{service}/v3/api-docs")
    public Mono<String> getServiceDocs(
            @Parameter(description = "Name of the microservice (e.g., user, product, order)", example = "user")
            @PathVariable String service
    ) {
        String serviceUrl = switch (service.toLowerCase()) {
            case "user" -> "http://localhost:8081/v3/api-docs";
            case "product" -> "http://localhost:8082/v3/api-docs";
            case "order" -> "http://localhost:8083/v3/api-docs";
            // Add remaining services here
            default -> throw new IllegalArgumentException("Unknown service: " + service);
        };

        return webClient.get()
                .uri(serviceUrl)
                .retrieve()
                .bodyToMono(String.class);
    }
}

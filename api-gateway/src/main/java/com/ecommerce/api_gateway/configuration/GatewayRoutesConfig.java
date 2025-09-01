package com.ecommerce.api_gateway.configuration;

import com.ecommerce.api_gateway.filter.JwtValidationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * API Gateway route configuration.
 * Defines routes to microservices and applies JWT validation where needed.
 */
@Slf4j
@Configuration
public class GatewayRoutesConfig {

    private final JwtValidationFilter jwtValidationFilter;

    public GatewayRoutesConfig(JwtValidationFilter jwtValidationFilter) {
        this.jwtValidationFilter = jwtValidationFilter;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        log.info("Initializing API Gateway Routes");

        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .filters(f -> f.rewritePath("/auth/(?<segment>.*)", "/${segment}"))
                        .uri("lb://auth-service"))
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> f.filter(jwtValidationFilter))
                        .uri("lb://user-service"))
                .route("cart-service", r -> r.path("/cart/**")
                        .filters(f -> f.filter(jwtValidationFilter))
                        .uri("lb://cart-service"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.filter(jwtValidationFilter))
                        .uri("lb://inventory-service"))
                .route("notification-service", r -> r.path("/notification/**")
                        .filters(f -> f.filter(jwtValidationFilter))
                        .uri("lb://notification-service"))
                .build();
    }
}

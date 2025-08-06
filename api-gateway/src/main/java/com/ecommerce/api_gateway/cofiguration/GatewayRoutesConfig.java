package com.ecommerce.api_gateway.cofiguration;

import com.ecommerce.api_gateway.filter.JwtValidationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining API Gateway routes.
 * This class configures the routing rules to forward incoming HTTP requests
 * to appropriate microservices and applies filters like JWT validation.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayRoutesConfig {

    private final JwtValidationFilter jwtValidationFilter;

    /**
     * Defines and builds the custom routes for the API Gateway using RouteLocatorBuilder.
     * Applies a JWT validation filter for secure routes like user-service.
     *
     * @param builder Spring's RouteLocatorBuilder for building custom routes
     * @return RouteLocator containing all defined routes and filters
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        log.info("✅ Initializing Gateway Routes");

        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("lb://auth-service"))
                .route("user-service", r -> r.path("/users/**")
                        .filters(f -> {
                            log.debug("🔒 Applying JWT filter to /users/** route");
                            return f.filter(jwtValidationFilter);
                        })
                        .uri("lb://user-service"))
                .route("cart-service", r -> r.path("/cart/**")
                .filters(f -> f.filter(jwtValidationFilter))  // ✅ Apply JWT validation at gateway
                .uri("lb://cart-service"))
                .route("inventory-service", r -> r.path("/inventory/**")
                        .filters(f -> f.filter(jwtValidationFilter)) // if you're protecting with JWT
                        .uri("lb://inventory-service"))
                .route("notification-service", r -> r.path("/notification/**")
                        .filters(f -> f.filter(jwtValidationFilter))
                        .uri("lb://notification-service"))
                .build();
    }

}

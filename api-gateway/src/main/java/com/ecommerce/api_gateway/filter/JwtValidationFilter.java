package com.ecommerce.api_gateway.filter;

import com.ecommerce.common_util.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * JwtValidationFilter is a Spring Cloud Gateway filter that performs validation
 * of the JWT token from the incoming request's Authorization header.
 * Responsibilities:
 * - Extracts the token from the header
 * - Validates the token using {@link JwtUtil}
 * - If invalid or missing, responds with HTTP 401 Unauthorized
 * - Allows valid requests to pass through the filter chain
 * This filter ensures that only authenticated requests proceed further into the application.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter implements GatewayFilter, Ordered {

    private final JwtUtil jwtUtil;

    /**
     * Filters each incoming request to:
     * - Check for the presence of a Bearer token in the Authorization header
     * - Validate the token using JwtUtil
     * - Block requests with missing or invalid tokens
     *
     * @param exchange the current server exchange
     * @param chain the gateway filter chain
     * @return Mono<Void> indicating completion of filter logic
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("🛡️ JwtValidationFilter is running");

        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("❗ Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtil.extractUsername(token);
            log.info("✅ JWT Token valid for user: {}", email);
        } catch (Exception e) {
            log.error("❌ Invalid JWT Token: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    /**
     * Defines the order in which this filter will be executed.
     * Lower numbers have higher priority. Setting to -1 to run early.
     *
     * @return the filter order
     */
    @Override
    public int getOrder() {
        return -1; // Run early in the filter chain
    }
}

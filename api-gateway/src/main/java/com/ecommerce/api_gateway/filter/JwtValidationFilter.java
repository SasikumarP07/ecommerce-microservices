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
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidationFilter implements GatewayFilter, Ordered {

    private final JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Allow public auth endpoints
        if (path.equals("/auth/signin") || path.equals("/auth/signup")) {
            log.info("🔓 Skipping JWT validation for public endpoint: {}", path);
            return chain.filter(exchange);
        }

        log.info("🛡️ Secured path: {}", path);

        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("❗ Missing/Invalid token");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            String username = jwtUtil.extractUsername(token);
            log.info("✅ Authenticated user: {}", username);
        } catch (Exception e) {
            log.error("❌ Invalid JWT: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // Run early in the filter chain
    }
}

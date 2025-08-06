package com.ecommerce.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * HeaderLoggingFilter is a global filter that runs for every incoming request through the API Gateway.
 * Responsibilities:
 * - Logs the Authorization header if present.
 * - Ensures the Authorization header is explicitly forwarded to downstream services.
 * - Executes early in the filter chain due to high priority (order = -1).
 */
@Slf4j
@Component
public class HeaderLoggingFilter implements GlobalFilter, Ordered {

    /**
     * Intercepts all incoming requests to:
     * - Log the Authorization header (if any).
     * - Forward the header to downstream services explicitly by mutating the request.
     *
     * @param exchange The current server exchange
     * @param chain The gateway filter chain
     * @return Mono<Void> indicating asynchronous processing completion
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("🔐 Forwarding Auth Header: {}", authHeader);

        if (authHeader != null) {
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        return chain.filter(exchange);
    }

    /**
     * Specifies the execution order of this filter.
     * Lower values are higher in priority.
     *
     * @return int - execution order of the filter (set to -1 to run early)
     */
    @Override
    public int getOrder() {
        return -1;
    }
}

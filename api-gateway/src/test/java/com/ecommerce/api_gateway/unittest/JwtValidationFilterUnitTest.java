package com.ecommerce.api_gateway.unittest;

import com.ecommerce.api_gateway.filter.JwtValidationFilter;
import com.ecommerce.common_util.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JwtValidationFilter.
 * This class tests the behavior of JwtValidationFilter under different scenarios:
 * 1. Missing Authorization header.
 * 2. Invalid JWT token.
 * 3. Valid JWT token.
 * Uses Mockito for mocking dependencies and StepVerifier for testing reactive flows.
 */
class JwtValidationFilterUnitTest {

    private JwtUtil jwtUtil;
    private JwtValidationFilter filter;

    /**
     * Set up the test environment by creating mock JwtUtil and initializing the filter with it.
     */
    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        filter = new JwtValidationFilter(jwtUtil);
    }

    /**
     * Test the scenario where the Authorization header is missing in the request.
     * Expected behavior: Response should be 401 UNAUTHORIZED and the filter chain should not be called.
     */
    @Test
    void testMissingAuthorizationHeader() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test").build();
        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
        verify(chain, never()).filter(exchange);
    }

    /**
     * Test the scenario where the JWT token is present but invalid.
     * Expected behavior: Response should be 401 UNAUTHORIZED and the filter chain should not be called.
     */
    @Test
    void testInvalidToken() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer invalid_token")
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        when(jwtUtil.extractUsername("invalid_token")).thenThrow(new RuntimeException("Invalid token"));

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        assert exchange.getResponse().getStatusCode() == HttpStatus.UNAUTHORIZED;
        verify(chain, never()).filter(exchange);
    }

    /**
     * Test the scenario where the JWT token is valid.
     * Expected behavior: The filter should allow the request to proceed by calling the next filter in the chain.
     */
    @Test
    void testValidToken() {
        MockServerHttpRequest request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer valid_token")
                .build();

        ServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilterChain chain = mock(GatewayFilterChain.class);

        when(jwtUtil.extractUsername("valid_token")).thenReturn("test@example.com");
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        Mono<Void> result = filter.filter(exchange, chain);

        StepVerifier.create(result)
                .verifyComplete();

        verify(chain, times(1)).filter(exchange);
    }
}

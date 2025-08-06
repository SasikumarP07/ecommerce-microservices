package com.ecommerce.api_gateway.integrationtest;

import com.ecommerce.api_gateway.configuration.TestJwtUtilConfig;
import com.ecommerce.common_util.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

/**
 * Integration tests for validating the JwtValidationFilter behavior in the API Gateway.
 * Uses a mocked JwtUtil (via TestJwtUtilConfig) to simulate various JWT scenarios.
 */
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {TestJwtUtilConfig.class}
)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class JwtValidationFilterIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Test case: When a valid JWT token is provided,
     * the filter should allow the request to pass through and return HTTP 200.
     */
    @Test
    void whenValidJwt_thenReturns200() {
        String token = "valid.jwt.token";
        when(jwtUtil.extractUsername(token)).thenReturn("user@example.com");

        webTestClient.get()
                .uri("/test-service/test")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isOk();
    }

    /**
     * Test case: When an invalid JWT token is provided,
     * the filter should reject the request and return HTTP 401 Unauthorized.
     */
    @Test
    void whenInvalidJwt_thenReturns401() {
        String token = "invalid.jwt.token";
        when(jwtUtil.extractUsername(token)).thenThrow(new RuntimeException("Invalid token"));

        webTestClient.get()
                .uri("/test-service/test")
                .header("Authorization", "Bearer " + token)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    /**
     * Test case: When no Authorization header is provided,
     * the filter should reject the request and return HTTP 401 Unauthorized.
     */
    @Test
    void whenNoAuthorizationHeader_thenReturns401() {
        webTestClient.get()
                .uri("/test-service/test")
                .exchange()
                .expectStatus().isUnauthorized();
    }
}

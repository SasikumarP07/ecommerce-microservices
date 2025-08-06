package com.ecommerce.api_gateway.configuration;

import com.ecommerce.common_util.util.JwtUtil;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration class for providing a mocked instance of JwtUtil.
 * This configuration is used only during testing to avoid using the actual JwtUtil logic,
 * allowing controlled behavior (e.g., returning dummy usernames or throwing exceptions)
 * in unit and integration tests.
 */
@TestConfiguration
public class TestJwtUtilConfig {

    /**
     * Provides a Mockito mock of JwtUtil to be used in the test context.
     * @return a mocked JwtUtil instance
     */
    @Bean
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }
}

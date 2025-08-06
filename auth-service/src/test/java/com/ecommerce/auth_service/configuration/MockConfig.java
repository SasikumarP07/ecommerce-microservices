package com.ecommerce.auth_service.configuration;

import com.ecommerce.auth_service.serviceImplementation.AuthServiceImplementation;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * ✅ Test configuration class used to define custom test-specific beans.
 * This class provides a mocked version of {@link AuthServiceImplementation}
 * for use in integration tests, allowing you to isolate the controller layer
 * from actual service logic.
 * This avoids the deprecated {@code @MockBean} annotation in Spring Boot 3.4+.
 */
@TestConfiguration
class MockConfig {

    /**
     * ✅ Provides a Mockito mock of {@link AuthServiceImplementation} as a Spring bean.
     * This mock will be injected into the application context during tests,
     * enabling controller-level testing without invoking real service logic.
     *
     * @return a mocked instance of AuthServiceImplementation
     */
    @Bean
    public AuthServiceImplementation authService() {
        return Mockito.mock(AuthServiceImplementation.class);
    }
}

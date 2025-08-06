package com.ecommerce.auth_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Smoke test to verify that the Spring application context
 * for Auth Service loads successfully without errors.
 */
@SpringBootTest
class AuthServiceApplicationTests {

	/**
	 * Test to ensure the Spring application context loads correctly.
	 * This helps catch any configuration or bean wiring issues at startup.
	 */
	@Test
	void contextLoads() {
		// This test will pass if the application context starts successfully.
	}
}

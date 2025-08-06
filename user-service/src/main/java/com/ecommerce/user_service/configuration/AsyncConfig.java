package com.ecommerce.user_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class to enable asynchronous method execution in the application.
 * <p>
 * This allows methods annotated with {@code @Async} to run in a separate thread,
 * enabling non-blocking behavior for long-running tasks.
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}

package com.ecommerce.inventory_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * ❗ ErrorDetails
 * <p>
 * A simple POJO used for sending structured error responses
 * to the client when exceptions occur in the application.
 * </p>
 *
 * <p>
 * Typically used by {@code @ControllerAdvice} to wrap exception details:
 * - timestamp: The time when the error occurred
 * - message: A user-friendly error message
 * - details: Additional context or request-related information
 * </p>
 */
@AllArgsConstructor
@Getter
public class ErrorDetails {

    /**
     * The date and time when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * A brief message explaining the error.
     */
    private String message;

    /**
     * Additional details about the error, such as the request path.
     */
    private String details;
}

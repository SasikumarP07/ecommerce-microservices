package com.ecommerce.cart_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * A simple POJO to represent error details returned in the API response
 * when an exception occurs.
 * This class is typically used in custom exception handling to provide
 * structured and meaningful error information to clients.
 */
@AllArgsConstructor
@Data
public class ErrorDetails {

    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * A human-readable error message.
     */
    private String message;

    /**
     * Additional details about the error, such as the request path.
     */
    private String details;
}

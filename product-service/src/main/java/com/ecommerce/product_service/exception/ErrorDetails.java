package com.ecommerce.product_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * This class represents the structure of error details sent in the response
 * when an exception is thrown in the Product Service.
 */
@AllArgsConstructor
@Getter
@Setter
public class ErrorDetails {

    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * A brief message describing the error.
     */
    private String message;

    /**
     * Detailed information about the error (e.g., request path or internal cause).
     */
    private String details;
}

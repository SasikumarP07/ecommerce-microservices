package com.ecommerce.payment_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a simplified structure for error responses returned by
 * the Product Service REST APIs.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP status code associated with the error (e.g., 404, 400, 500).
     */
    private int status;

    /**
     * Human-readable message describing the error.
     */
    private String message;
}

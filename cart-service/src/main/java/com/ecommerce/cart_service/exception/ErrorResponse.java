package com.ecommerce.cart_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a simplified error response returned by the API.
 * This class is used to send a consistent structure for error messages,
 * typically containing the HTTP status code and a brief message.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP status code representing the error (e.g., 404, 500).
     */
    private int status;

    /**
     * A brief, user-friendly message describing the error.
     */
    private String message;
}

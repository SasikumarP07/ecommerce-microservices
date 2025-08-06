package com.ecommerce.order_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a standard error response returned from the Order Service API
 * when an exception occurs. It encapsulates the HTTP status code and
 * the corresponding error message to be sent to the client.
 * <p>
 * Example JSON response:
 * <pre>
 * {
 *   "status": 404,
 *   "message": "Order not found"
 * }
 * </pre>
 * </p>
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The HTTP status code of the error (e.g., 400, 404, 500).
     */
    private int status;

    /**
     * A human-readable message providing more details about the error.
     */
    private String message;

    // Lombok generates constructor, getters, and setters
}

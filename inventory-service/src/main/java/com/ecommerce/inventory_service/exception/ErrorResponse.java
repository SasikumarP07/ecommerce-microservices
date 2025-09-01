package com.ecommerce.inventory_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ErrorResponse
 * <p>
 * A simplified error response model returned to the client when an exception occurs.
 * Typically used in REST APIs to indicate HTTP status and error message.
 * </p>
 *
 * <p>
 * Example JSON Response:
 * <pre>
 * {
 *   "status": 404,
 *   "message": "Inventory not found for productId: 123"
 * }
 * </pre>
 * </p>
 *
 * Annotated with Lombok {@code @Data} and {@code @AllArgsConstructor} for boilerplate reduction.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The HTTP status code to return (e.g., 404, 400, 500).
     */
    private int status;

    /**
     * A user-friendly error message describing what went wrong.
     */
    private String message;
}

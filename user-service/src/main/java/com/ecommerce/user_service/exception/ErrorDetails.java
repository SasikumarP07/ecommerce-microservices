package com.ecommerce.user_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * ErrorDetails is a simple POJO used to encapsulate error information
 * returned to the client when an exception occurs in the application.
 * <p>
 * It includes the timestamp of the error, a message describing the error,
 * and additional details (such as the request path).
 * </p>
 *
 * This class is typically used in a centralized exception handling mechanism
 * (e.g., {@code @ControllerAdvice} with {@code @ExceptionHandler}).
 * Lombok annotations:
 * - {@code @Data} generates getters, setters, equals, hashCode, and toString.
 * - {@code @AllArgsConstructor} generates a constructor with all fields.
 * Example usage in a controller advice:
 * <pre>{@code
 *   @ExceptionHandler(ResourceNotFoundException.class)
 *   public ResponseEntity<ErrorDetails> handleNotFound(ResourceNotFoundException ex, WebRequest request) {
 *       ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
 *       return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
 *   }
 * }</pre>
 *
 */
@Data
@AllArgsConstructor
public class ErrorDetails {

    /**
     * The timestamp when the error occurred.
     */
    private LocalDateTime timestamp;

    /**
     * The error message describing what went wrong.
     */
    private String message;

    /**
     * Additional details about the error, typically the request URI or context information.
     */
    private String details;
}

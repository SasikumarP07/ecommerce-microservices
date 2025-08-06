package com.ecommerce.order_service.exception;

/**
 * Custom exception thrown when a user attempts to access a resource or perform
 * an operation for which they do not have the required authorization.
 * This exception typically results in a 401 Unauthorized or 403 Forbidden response.
 * Example usage:
 * <pre>
 * if (!order.getUserId().equals(currentUserId)) {
 *     throw new UnauthorizedAccessException("You are not authorized to access this order.");
 * }
 * </pre>
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

package com.ecommerce.cart_service.exception;

/**
 * Custom exception thrown when a user attempts to access a resource
 * they are not authorized to interact with.
 * This could be used for operations on a cart that belongs to a different user.
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message The detail message explaining the unauthorized access.
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

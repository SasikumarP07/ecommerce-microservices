package com.ecommerce.cart_service.exception;

/**
 * Custom exception thrown when a requested resource is not found.
 * Typically used to indicate that an entity such as Cart or CartItem
 * does not exist in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining which resource was not found.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

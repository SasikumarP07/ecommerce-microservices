package com.ecommerce.product_service.exception;

/**
 * Exception thrown when an attempt is made to create or add a resource
 * that already exists in the system (e.g., duplicate product name, SKU, etc.).
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}

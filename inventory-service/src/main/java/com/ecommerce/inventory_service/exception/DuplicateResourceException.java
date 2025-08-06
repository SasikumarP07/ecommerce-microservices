package com.ecommerce.inventory_service.exception;

/**
 * ❗ DuplicateResourceException
 * <p>
 * This exception is thrown when an attempt is made to create a resource
 * that already exists in the system (e.g., trying to create an inventory
 * entry for a product that already has one).
 * </p>
 *
 * <p>
 * It extends {@link RuntimeException}, so it is an unchecked exception.
 * </p>
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message that explains the reason for the exception
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}

package com.ecommerce.inventory_service.exception;

/**
 * Exception thrown when a user attempts to access a resource or perform an operation
 * without the necessary authorization.
 * <p>
 * This exception is typically used to indicate access control violations in the
 * Inventory Service.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * if (!user.hasPermission("UPDATE_INVENTORY")) {
 *     throw new UnauthorizedAccessException("User does not have permission to update inventory.");
 * }
 * </pre>
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

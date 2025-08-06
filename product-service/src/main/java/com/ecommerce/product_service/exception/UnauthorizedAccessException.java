package com.ecommerce.product_service.exception;

/**
 * Exception thrown when a user tries to perform an operation without the necessary authorization.
 * <p>
 * This can occur in scenarios such as:
 * <ul>
 *     <li>Attempting to modify a product without proper roles or permissions</li>
 *     <li>Accessing restricted endpoints without valid authentication</li>
 *     <li>Trying to delete or update resources owned by another user</li>
 * </ul>
 *
 * <p>This class extends {@link RuntimeException}, making it an unchecked exception.
 */
public class UnauthorizedAccessException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message explaining the access violation
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}

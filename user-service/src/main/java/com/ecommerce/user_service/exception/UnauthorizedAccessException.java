package com.ecommerce.user_service.exception;

/**
 * Exception thrown when a user attempts to access a resource or perform an action
 * for which they do not have the necessary permissions or authentication.
 *
 * <p>This is a custom unchecked exception (extends {@link RuntimeException}).</p>
 *
 * Example use case:
 * <pre>
 *     if (!currentUserId.equals(resourceOwnerId)) {
 *         throw new UnauthorizedAccessException("You are not authorized to access this resource.");
 *     }
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

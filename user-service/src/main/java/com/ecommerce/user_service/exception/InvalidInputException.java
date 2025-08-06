package com.ecommerce.user_service.exception;

/**
 * Exception thrown when the user provides invalid input data.
 * This can be used to signal issues such as missing required fields,
 * invalid formats, or business rule violations.
 *
 * <p>This is a custom unchecked exception (extends {@link RuntimeException}).</p>
 *
 * Example usage:
 * <pre>
 *     if (user.getEmail() == null) {
 *         throw new InvalidInputException("Email must not be null");
 *     }
 * </pre>
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidInputException(String message) {
        super(message);
    }
}

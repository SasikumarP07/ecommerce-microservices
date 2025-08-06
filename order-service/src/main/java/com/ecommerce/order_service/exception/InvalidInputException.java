package com.ecommerce.order_service.exception;

/**
 * Custom exception thrown when an invalid input is provided
 * during order processing or validation.
 * This exception extends {@link RuntimeException}, allowing it
 * to be thrown without being explicitly declared.
 * Example usage:
 * <pre>
 * if (orderQuantity <= 0) {
 *     throw new InvalidInputException("Quantity must be greater than 0");
 * }
 * </pre>
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public InvalidInputException(String message) {
        super(message);
    }
}

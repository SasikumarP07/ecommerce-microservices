package com.ecommerce.inventory_service.exception;

/**
 * Custom exception thrown when an invalid input is provided to the Inventory Service.
 * <p>
 * This exception is typically used to indicate that the provided data does not meet
 * the required validation criteria or business rules.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * if (quantity < 0) {
 *     throw new InvalidInputException("Quantity must be a non-negative number");
 * }
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

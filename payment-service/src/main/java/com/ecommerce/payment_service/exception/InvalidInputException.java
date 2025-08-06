package com.ecommerce.payment_service.exception;

/**
 * Exception thrown when an invalid input is encountered in the Product Service.
 * This exception is typically used for validating request data or user inputs
 * that do not meet the expected format or criteria.
 *
 * <p>Example scenarios:
 * <ul>
 *     <li>Empty or null product name</li>
 *     <li>Negative price or stock values</li>
 *     <li>Invalid image URL format</li>
 * </ul>
 *
 * This class extends {@link RuntimeException}, so it is an unchecked exception.
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the specified detail message.
     *
     * @param message the detail message describing the validation error
     */
    public InvalidInputException(String message) {
        super(message);
    }
}

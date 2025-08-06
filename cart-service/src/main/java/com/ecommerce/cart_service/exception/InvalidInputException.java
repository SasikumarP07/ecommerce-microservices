package com.ecommerce.cart_service.exception;

/**
 * Custom exception thrown when invalid input is provided by the client.
 * Typically used to indicate bad request scenarios where input fails validation.
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

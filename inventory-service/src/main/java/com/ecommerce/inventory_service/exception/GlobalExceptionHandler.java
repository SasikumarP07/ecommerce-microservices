package com.ecommerce.inventory_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * 🚨 GlobalExceptionHandler
 * <p>
 * Centralized exception handling for all controllers in the application.
 * Catches and handles specific and generic exceptions thrown by service or controller layers,
 * returning consistent and meaningful error responses to the client.
 * </p>
 *
 * <p>
 * Annotated with {@code @RestControllerAdvice} to apply globally across all controllers.
 * Uses {@code @ExceptionHandler} methods to handle various exception types.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles exceptions when a requested resource is not found.
     *
     * @param ex the ResourceNotFoundException
     * @return HTTP 404 with error details
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("ResourceNotFoundException: {}", ex.getMessage());
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Resource Not Found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions when input validation fails or input is invalid.
     *
     * @param ex the InvalidInputException
     * @return HTTP 400 with error details
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDetails> handleInvalidInput(InvalidInputException ex) {
        logger.warn("InvalidInputException: {}", ex.getMessage());
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Invalid Input");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles general unhandled exceptions.
     *
     * @param ex the Exception
     * @return HTTP 500 with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Server Error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles unauthorized access attempts (e.g., missing or invalid JWT).
     *
     * @param ex the UnauthorizedAccessException
     * @return HTTP 403 with a simplified error response
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        logger.warn("UnauthorizedAccessException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles validation errors for request DTOs annotated with {@code @Valid}.
     *
     * @param ex the MethodArgumentNotValidException
     * @return HTTP 400 with the first validation error message
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        logger.warn("Validation failed: {}", message);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where a duplicate resource (e.g., inventory or user) is being created.
     *
     * @param ex the DuplicateResourceException
     * @return HTTP 409 Conflict with error message
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
        logger.warn("DuplicateResourceException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}

package com.ecommerce.order_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global exception handler for the Order Service.
 * This class handles exceptions thrown across the entire application and returns meaningful error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles {@link ResourceNotFoundException} and returns a 404 NOT FOUND status.
     *
     * @param ex the ResourceNotFoundException
     * @return ResponseEntity with error details and 404 status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFound(ResourceNotFoundException ex) {
        logger.warn("ResourceNotFoundException: {}", ex.getMessage());
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Resource Not Found");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link InvalidInputException} and returns a 400 BAD REQUEST status.
     *
     * @param ex the InvalidInputException
     * @return ResponseEntity with error details and 400 status
     */
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorDetails> handleInvalidInput(InvalidInputException ex) {
        logger.warn("InvalidInputException: {}", ex.getMessage());
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Invalid Input");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles all uncaught exceptions and returns a 500 INTERNAL SERVER ERROR status.
     *
     * @param ex the Exception
     * @return ResponseEntity with error details and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGenericException(Exception ex) {
        logger.error("Unhandled exception: {}", ex.getMessage(), ex);
        ErrorDetails error = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), "Server Error");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link UnauthorizedAccessException} and returns a 403 FORBIDDEN status.
     *
     * @param ex the UnauthorizedAccessException
     * @return ResponseEntity with error message and 403 status
     */
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        logger.warn("UnauthorizedAccessException: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles validation errors from request body using {@link MethodArgumentNotValidException}
     * and returns a 400 BAD REQUEST status.
     *
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity with validation error message and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldError().getDefaultMessage();
        logger.warn("Validation failed: {}", message);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}

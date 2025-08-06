package com.ecommerce.user_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * ErrorResponse is a simple POJO class that represents error information
 * returned to the client in case of API failures.
 * <p>
 * It contains the HTTP status code and an error message to describe the issue.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {

    /**
     * HTTP status code of the error (e.g., 404, 500).
     */
    private int status;

    /**
     * Description or message related to the error.
     */
    private String message;

    // Lombok generates constructor, getters, and setters
}

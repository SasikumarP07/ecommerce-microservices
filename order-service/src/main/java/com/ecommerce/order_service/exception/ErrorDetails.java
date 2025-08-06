package com.ecommerce.order_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents error details to be returned in an API error response.
 * Contains the timestamp, error message, and request-specific details.
 */
@Data
@AllArgsConstructor
public class ErrorDetails {

    private LocalDateTime timestamp;
    private String message;
    private String details;
}

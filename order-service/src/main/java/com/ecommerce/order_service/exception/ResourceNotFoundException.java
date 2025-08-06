package com.ecommerce.order_service.exception;

/**
 * Custom exception thrown when a requested resource (e.g., Order, OrderItem)
 * is not found in the system.
 * This exception typically results in a 404 Not Found response.
 * Example usage:
 * <pre>
 * Order order = orderRepository.findById(orderId)
 *     .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

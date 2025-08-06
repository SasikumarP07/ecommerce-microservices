package com.ecommerce.user_service.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 * This is typically used when querying for an entity (e.g., User, Address)
 * by ID or another identifier, and no match is found.
 *
 * <p>This is a custom unchecked exception (extends {@link RuntimeException}).</p>
 *
 * Example usage:
 * <pre>
 *     Optional<User> user = userRepository.findById(id);
 *     if (user.isEmpty()) {
 *         throw new ResourceNotFoundException("User not found with ID: " + id);
 *     }
 * </pre>
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

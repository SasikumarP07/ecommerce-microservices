package com.ecommerce.product_service.exception;

/**
 * Exception thrown when a requested resource is not found in the Product Service.
 * This can occur when trying to access, update, or delete a product, category,
 * or other entities that do not exist in the database.
 *
 * <p>Example scenarios:
 * <ul>
 *     <li>Product ID not found</li>
 *     <li>Category does not exist</li>
 *     <li>Requested image or resource file is missing</li>
 * </ul>
 *
 * This class extends {@link RuntimeException}, making it an unchecked exception.
 */
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the missing resource
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

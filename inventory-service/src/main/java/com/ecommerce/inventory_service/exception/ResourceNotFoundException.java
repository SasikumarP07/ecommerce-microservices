package com.ecommerce.inventory_service.exception;

/**
 * Exception thrown when a requested resource is not found in the Inventory Service.
 * <p>
 * This exception is used to indicate that an entity such as a product or inventory item
 * could not be located in the database.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Optional&lt;Inventory&gt; inventory = inventoryRepository.findById(id);
 * if (inventory.isEmpty()) {
 *     throw new ResourceNotFoundException("Inventory with ID " + id + " not found");
 * }
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

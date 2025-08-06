package com.ecommerce.inventory_service.service;

import com.ecommerce.common_dto.dto.inventory.*;

/**
 * Service interface for managing inventory-related operations.
 * <p>
 * This interface defines the contract for creating, retrieving, updating,
 * and reducing inventory stock for products in the system.
 * </p>
 */
public interface InventoryService {

    /**
     * Creates a new inventory record for a product.
     *
     * @param requestDTO the details of the inventory to be created
     * @return the created {@link InventoryResponseDTO} containing inventory information
     */
    InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO);

    /**
     * Retrieves the inventory details for a given product ID.
     *
     * @param productId the ID of the product
     * @return the corresponding {@link InventoryResponseDTO} if found
     */
    InventoryResponseDTO getInventoryByProductId(Long productId);

    /**
     * Updates the stock quantity of an existing inventory record.
     *
     * @param productId   the ID of the product
     * @param newQuantity the new quantity to be set
     * @return the updated {@link InventoryResponseDTO}
     */
    InventoryResponseDTO updateStock(Long productId, int newQuantity);

    /**
     * Reduces the stock quantity of a product by a specified amount.
     *
     * @param productId the ID of the product
     * @param quantity  the quantity to be reduced
     */
    void reduceStock(Long productId, int quantity);
}

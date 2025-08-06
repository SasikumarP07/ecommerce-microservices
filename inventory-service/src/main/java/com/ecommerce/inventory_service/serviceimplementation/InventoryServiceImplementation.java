package com.ecommerce.inventory_service.serviceimplementation;

import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.common_dto.dto.inventory.InventoryResponseDTO;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.exception.ResourceNotFoundException;
import com.ecommerce.inventory_service.mapper.InventoryMapper;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link InventoryService} interface.
 * <p>
 * Provides logic for creating inventory entries, retrieving stock levels,
 * updating inventory quantities, and reducing stock based on transactions.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImplementation implements InventoryService {

    private final InventoryRepository inventoryRepository;

    /**
     * Creates a new inventory record for a given product.
     *
     * @param requestDTO DTO containing product ID and initial quantity
     * @return the saved inventory data as {@link InventoryResponseDTO}
     */
    @Override
    public InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO) {
        log.info("Creating inventory for productId: {}", requestDTO.getProductId());

        Inventory inventory = Inventory.builder()
                .productId(requestDTO.getProductId())
                .quantity(requestDTO.getQuantity())
                .build();

        Inventory saved = inventoryRepository.save(inventory);

        log.debug("Inventory saved: {}", saved);
        return InventoryMapper.toDTO(saved);
    }

    /**
     * Retrieves inventory data for a specific product.
     *
     * @param productId the product ID to query
     * @return the inventory details as {@link InventoryResponseDTO}
     * @throws ResourceNotFoundException if inventory for the product doesn't exist
     */
    @Override
    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for productId: {}", productId);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("Inventory not found for productId: {}", productId);
                    return new ResourceNotFoundException("Inventory not found for productId " + productId);
                });

        log.debug("Inventory found: {}", inventory);
        return InventoryMapper.toDTO(inventory);
    }

    /**
     * Updates the stock quantity of a specific product.
     *
     * @param productId   the product ID whose stock needs to be updated
     * @param newQuantity the new quantity to set
     * @return the updated inventory details
     * @throws ResourceNotFoundException if inventory for the product doesn't exist
     */
    @Override
    public InventoryResponseDTO updateStock(Long productId, int newQuantity) {
        log.info("Updating stock for productId: {} with new quantity: {}", productId, newQuantity);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("Inventory not found for productId: {}", productId);
                    return new ResourceNotFoundException("Inventory not found for productId " + productId);
                });

        inventory.setQuantity(newQuantity);
        Inventory updated = inventoryRepository.save(inventory);

        log.debug("Stock updated: {}", updated);
        return InventoryMapper.toDTO(updated);
    }

    /**
     * Reduces the stock quantity of a product.
     *
     * @param productId the product ID to reduce stock from
     * @param quantity  the quantity to subtract
     * @throws ResourceNotFoundException if inventory for the product doesn't exist
     * @throws IllegalArgumentException  if requested quantity exceeds available stock
     */
    @Override
    @Transactional
    public void reduceStock(Long productId, int quantity) {
        log.info("Reducing stock for productId: {} by quantity: {}", productId, quantity);

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("Inventory not found for productId: {}", productId);
                    return new ResourceNotFoundException("Inventory not found for productId " + productId);
                });

        if (inventory.getQuantity() < quantity) {
            log.warn("Insufficient stock for productId: {}. Available: {}, Requested: {}",
                    productId, inventory.getQuantity(), quantity);
            throw new IllegalArgumentException("Insufficient stock for productId " + productId);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        log.info("Stock successfully reduced for productId: {}", productId);
    }
}

package com.ecommerce.inventory_service.mapper;

import com.ecommerce.common_dto.dto.inventory.InventoryResponseDTO;
import com.ecommerce.inventory_service.entity.Inventory;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class responsible for mapping Inventory entities to DTOs.
 * <p>
 * This mapper is used to convert {@link Inventory} entities into
 * {@link InventoryResponseDTO} objects for outbound communication
 * (e.g., API responses).
 * </p>
 */
@Slf4j
public class InventoryMapper {

    /**
     * Converts an {@link Inventory} entity to an {@link InventoryResponseDTO}.
     *
     * @param inventory the Inventory entity to convert
     * @return the corresponding InventoryResponseDTO
     */
    public static InventoryResponseDTO toDTO(Inventory inventory) {
        log.info("Mapping Inventory entity to InventoryResponseDTO for productId: {}", inventory.getProductId());
        return InventoryResponseDTO.builder()
                .productId(inventory.getProductId())
                .stockQuantity(inventory.getQuantity())
                .build();
    }
}

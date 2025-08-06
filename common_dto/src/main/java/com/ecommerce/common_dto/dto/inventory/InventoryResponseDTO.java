package com.ecommerce.common_dto.dto.inventory;

import lombok.*;

/**
 * DTO used to send inventory information in responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDTO {

    /**
     * Unique ID of the inventory record.
     */
    private Long id;

    /**
     * Associated product ID.
     */
    private Long productId;

    /**
     * Available quantity in stock.
     */
    private Integer stockQuantity;
}

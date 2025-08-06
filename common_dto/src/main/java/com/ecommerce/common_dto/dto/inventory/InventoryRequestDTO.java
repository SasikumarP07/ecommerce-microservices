package com.ecommerce.common_dto.dto.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for sending inventory creation or update requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequestDTO {

    /**
     * Product ID to which the inventory is linked.
     */
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    /**
     * Available quantity of the product.
     */
    @NotNull(message = "Stock quantity cannot be null")
    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer quantity;
}

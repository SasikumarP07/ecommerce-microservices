package com.ecommerce.common_dto.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for requesting a single order item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDTO {

    /**
     * ID of the product to be ordered.
     */
    @NotNull(message = "Product ID must not be null")
    private Long productId;

    /**
     * Quantity of the product being ordered.
     * Must be at least 1.
     */
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

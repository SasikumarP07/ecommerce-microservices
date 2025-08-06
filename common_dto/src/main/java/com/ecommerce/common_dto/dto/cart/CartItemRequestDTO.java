package com.ecommerce.common_dto.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for adding or updating an item in the cart.
 * This DTO is used to send product ID and quantity from the client to the backend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDTO {

    /**
     * The ID of the product to be added or updated in the cart.
     * Must not be null.
     */
    @NotNull(message = "Product ID must not be null")
    private Long productId;

    /**
     * The quantity of the product.
     * Must be at least 1 and not null.
     */
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

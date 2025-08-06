package com.ecommerce.common_dto.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used for updating the quantity of an item in the cart.
 * It requires a valid product ID and a new quantity value.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemUpdateDTO {

    /**
     * Unique identifier of the product to be updated in the cart.
     */
    @NotNull(message = "Product ID must not be null")
    private Long productId;

    /**
     * New quantity of the product. Must be at least 1.
     */
    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}

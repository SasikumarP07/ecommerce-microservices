package com.ecommerce.common_dto.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for sending cart item details in the response.
 * This DTO includes product and pricing information for each item in the cart.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDTO {

    /**
     * Unique identifier of the cart item.
     */
    private Long id;

    /**
     * Unique identifier of the product.
     */
    private Long productId;

    /**
     * Name of the product.
     */
    private String productName;

    /**
     * Price per unit of the product.
     */
    private BigDecimal productPrice;

    /**
     * Quantity of the product in the cart.
     */
    private Integer quantity;

    /**
     * Total price = productPrice * quantity.
     */
    private BigDecimal totalPrice;
}

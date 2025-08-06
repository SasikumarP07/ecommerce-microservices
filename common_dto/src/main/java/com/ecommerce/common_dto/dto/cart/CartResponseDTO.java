package com.ecommerce.common_dto.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents the response object for a user's cart, including all items and the total price.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    /**
     * Unique identifier for the cart.
     */
    private Long cartId;

    /**
     * ID of the user who owns the cart.
     */
    private Long userId;

    /**
     * List of items present in the cart.
     */
    private List<CartItemResponseDTO> items;

    /**
     * Total price of all items in the cart.
     */
    private BigDecimal totalPrice;


}

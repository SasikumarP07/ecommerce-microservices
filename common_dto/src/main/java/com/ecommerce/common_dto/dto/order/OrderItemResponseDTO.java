package com.ecommerce.common_dto.dto.order;

import lombok.*;

import java.math.BigDecimal;

/**
 * Response DTO representing individual items in an order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {

    /**
     * Unique ID for the order item.
     */
    private Long id;

    /**
     * ID of the product associated with this order item.
     */
    private Long productId;

    /**
     * Name of the product.
     */
    private String productName;

    /**
     * Price of a single product unit at the time of ordering.
     */
    private BigDecimal productPrice;

    /**
     * Quantity of the product ordered.
     */
    private Integer quantity;

    /**
     * Total price = productPrice * quantity.
     */
    private BigDecimal totalPrice;
}

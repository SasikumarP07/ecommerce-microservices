package com.ecommerce.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents an item in a customer's order.
 * Each item corresponds to a specific product with its own quantity and pricing details.
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    /**
     * Unique identifier for the order item.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID of the product associated with this order item.
     */
    private Long productId;

    /**
     * Name of the product at the time of order.
     */
    private String productName;

    /**
     * Price of the product at the time of order.
     */
    private BigDecimal productPrice;

    /**
     * Quantity of the product ordered.
     */
    private Integer quantity;

    /**
     * Total price for this item (productPrice * quantity).
     */
    private BigDecimal totalPrice;

    /**
     * The order to which this item belongs.
     * Many items can belong to a single order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}

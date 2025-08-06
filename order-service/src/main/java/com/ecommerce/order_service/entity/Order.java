package com.ecommerce.order_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a customer's order in the e-commerce system.
 * Each order contains details such as the user who placed the order,
 * the total amount, order date, status, and the list of items in the order.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    /**
     * Primary key of the order.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ID of the user who placed the order.
     */
    private Long userId;

    /**
     * Total cost of the order.
     */
    private BigDecimal totalAmount;

    /**
     * Timestamp when the order was placed.
     */
    private LocalDateTime orderDate;

    /**
     * Current status of the order (e.g., PENDING, COMPLETED, CANCELLED).
     */
    private String status;

    /**
     * List of items included in the order.
     * Each item is associated with this order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
}

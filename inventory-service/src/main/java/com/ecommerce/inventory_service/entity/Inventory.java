package com.ecommerce.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Inventory Entity
 * <p>
 * Represents the inventory record for a specific product in the database.
 * Each product has a one-to-one mapping with its inventory.
 * </p>
 *
 * <p>
 * This entity is stored in the `inventory` table with the following fields:
 * - productId: Unique identifier of the product (also used as the primary key)
 * - quantity: Available stock for the product
 * </p>
 *
 *  Uses Lombok annotations for boilerplate code generation:
 * - {@code @Data} for getters, setters, equals, hashCode, and toString
 * - {@code @Builder} for fluent object creation
 * - {@code @NoArgsConstructor}, {@code @AllArgsConstructor} for constructors
 */
@Entity
@Table(name = "inventory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    /**
     * The unique identifier of the product.
     * Also serves as the primary key in the inventory table.
     */
    @Id
    private Long productId;

    /**
     * The number of units currently in stock for the product.
     */
    private Integer quantity;
}

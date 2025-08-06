package com.ecommerce.cart_service.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents an item within a shopping cart.
 * Each CartItem holds a reference to a specific product and its quantity,
 * and is associated with a {@link Cart}.
 * This entity maps to the "cart_items" table in the database.
 * Lombok annotations are used for boilerplate code generation.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "cart") // Exclude cart from toString to prevent lazy-loading issues or stack overflow
@Entity
@Table(name = "cart_items")
public class CartItem {

    /**
     * The unique identifier for the cart item.
     * This is the primary key and is auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the product this cart item refers to.
     */
    private Long productId;

    /**
     * The quantity of the product in the cart.
     */
    private Integer quantity;

    /**
     * The cart to which this item belongs.
     * A many-to-one relationship indicating that many CartItems belong to one Cart.
     * Lazy fetch type is used to avoid unnecessary loading of the Cart entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
}

package com.ecommerce.cart_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a shopping cart associated with a specific user.
 * This entity maintains a list of {@link CartItem} entities,
 * where each cart item corresponds to a product added by the user.
 * JPA annotations are used to map this entity to the "carts" table in the database.
 * Lombok annotations are used to generate boilerplate code such as getters, setters, constructors, and builder pattern.
 */
@Entity
@Table(name = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    /**
     * The unique identifier for the cart.
     * This is the primary key and is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The identifier of the user who owns this cart.
     */
    private Long userId;

    /**
     * The list of items in the cart.
     * One-to-many relationship with {@link CartItem}.
     * Cascade operations ensure changes to Cart propagate to CartItem.
     * Orphan removal ensures CartItems are deleted when removed from the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;

    /**
     * Total price of all items in the cart.
     */
    private BigDecimal totalPrice;

}

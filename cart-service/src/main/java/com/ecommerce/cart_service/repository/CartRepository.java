package com.ecommerce.cart_service.repository;

import com.ecommerce.cart_service.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link Cart} entities.
 * This interface provides CRUD operations and a custom query method
 * for retrieving a cart by the associated user ID.
 * Extends {@link JpaRepository}, which provides:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 * - and more.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds the cart associated with a specific user.
     *
     * @param userId The ID of the user whose cart is to be retrieved.
     * @return An {@link Optional} containing the {@link Cart}, if found.
     */
    Optional<Cart> findByUserId(Long userId);
}

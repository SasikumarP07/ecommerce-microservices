package com.ecommerce.cart_service.repository;

import com.ecommerce.cart_service.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 📦 Repository interface for managing {@link CartItem} entities.
 * This interface provides basic CRUD operations for CartItem objects.
 * It extends {@link JpaRepository} which includes methods such as:
 * - save()
 * - findById()
 * - findAll()
 * - deleteById()
 * Spring Data JPA will automatically generate the implementation at runtime.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}

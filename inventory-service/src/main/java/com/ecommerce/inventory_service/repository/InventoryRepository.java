package com.ecommerce.inventory_service.repository;

import com.ecommerce.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing {@link Inventory} entities from the database.
 * <p>
 * Extends {@link JpaRepository} to provide CRUD operations and custom query methods.
 * </p>
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Retrieves an {@link Inventory} record based on the product ID.
     *
     * @param productId the ID of the product
     * @return an {@link Optional} containing the matching Inventory entity, if found
     */
    Optional<Inventory> findByProductId(Long productId);
}

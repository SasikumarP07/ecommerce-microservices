package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Product} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations,
 * and {@link JpaSpecificationExecutor} to support dynamic filtering and complex queries.
 * </p>
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Additional query methods can be defined here as needed.
}

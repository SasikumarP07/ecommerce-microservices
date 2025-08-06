package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link OrderItem} entities.
 * <p>
 * This interface provides basic CRUD operations and query methods for the OrderItem entity.
 * It extends {@link JpaRepository}, which includes methods like save, findById, findAll, deleteById, etc.
 * <p>
 * Spring Data JPA will automatically generate the implementation at runtime.
 *
 * @author YourName
 * @see OrderItem
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // You can define custom query methods here if needed
}

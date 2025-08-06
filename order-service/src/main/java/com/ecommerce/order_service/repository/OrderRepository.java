package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for managing {@link Order} entities.
 * <p>
 * This interface provides CRUD operations and custom query methods for Order entities.
 * It extends {@link JpaRepository}, which includes methods like save, findById, findAll, deleteById, etc.
 * <p>
 * The method {@code findByUserId(Long userId)} allows retrieval of all orders placed by a specific user.
 * <p>
 * Spring Data JPA automatically implements this interface at runtime.
 *
 * @author YourName
 * @see Order
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a list of orders associated with the specified user ID.
     *
     * @param userId the ID of the user whose orders should be fetched
     * @return a list of {@link Order} entities
     */
    List<Order> findByUserId(Long userId);
}

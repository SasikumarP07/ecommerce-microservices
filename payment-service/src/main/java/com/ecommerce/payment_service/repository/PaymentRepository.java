package com.ecommerce.payment_service.repository;

import com.ecommerce.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing and managing {@link Payment} entities.
 * <p>
 * Extends {@link JpaRepository} to provide basic CRUD operations and includes
 * a custom query method to find a payment by its associated order ID.
 * </p>
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Retrieves a payment entity by its associated order ID.
     *
     * @param orderId the ID of the order
     * @return the {@link Payment} entity associated with the given order ID, or {@code null} if not found
     */
    Payment findByOrderId(Long orderId);
}

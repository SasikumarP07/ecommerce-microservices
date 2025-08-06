package com.ecommerce.payment_service.entity;


import com.ecommerce.common_dto.enums.PaymentMethod;
import com.ecommerce.common_dto.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a payment record in the system.
 * <p>
 * Each payment is linked to an order and contains information such as
 * the amount, method, status, and timestamp.
 * </p>
 */
@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    /**
     * The unique identifier for the payment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The ID of the order associated with this payment.
     */
    private Long orderId;

    /**
     * The total amount paid.
     */
    private BigDecimal amount;

    /**
     * The method used for the payment (e.g., CARD, UPI, NETBANKING).
     */
    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    /**
     * The current status of the payment (e.g., SUCCESS, FAILED).
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * The timestamp when the payment was made.
     */
    private LocalDateTime timestamp;
}

package com.ecommerce.common_dto.dto.payment;


import com.ecommerce.common_dto.enums.PaymentMethod;
import com.ecommerce.common_dto.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO representing the response for a completed payment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    /**
     * Unique payment ID.
     */
    private Long id;

    /**
     * The associated order ID.
     */
    private Long orderId;

    /**
     * Total payment amount.
     */
    private BigDecimal amount;

    /**
     * Payment status (e.g., SUCCESS, FAILED, PENDING).
     */
    private PaymentStatus status;

    /**
     * Method used for payment (e.g., UPI, CARD, COD).
     */
    private PaymentMethod method;

    /**
     * Timestamp of the payment transaction.
     */
    private LocalDateTime timestamp;
}

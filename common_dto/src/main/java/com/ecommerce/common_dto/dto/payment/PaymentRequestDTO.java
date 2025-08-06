package com.ecommerce.common_dto.dto.payment;

import com.ecommerce.common_dto.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO representing a payment request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {

    /**
     * ID of the order being paid for.
     */
    @NotNull(message = "Order ID must not be null")
    private Long orderId;

    /**
     * Payment amount.
     */
    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    /**
     * Payment method (e.g., CREDIT_CARD, UPI, etc.)
     */
    @NotNull(message = "Payment method must not be null")
    private PaymentMethod paymentMethod;
}

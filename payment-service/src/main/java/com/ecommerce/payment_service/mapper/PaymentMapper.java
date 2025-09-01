package com.ecommerce.payment_service.mapper;

import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for mapping between Payment entity and DTOs.
 * <p>
 * Handles transformation from {@link PaymentRequestDTO} to {@link Payment}
 * and from {@link Payment} to {@link PaymentResponseDTO}.
 * </p>
 */
@Slf4j
public class PaymentMapper {

    /**
     * Converts a {@link PaymentRequestDTO} to a {@link Payment} entity.
     * <p>
     * This method does not set the {@code status} or {@code timestamp} fields.
     * These are expected to be set during the actual processing of the payment.
     * </p>
     *
     * @param dto the payment request DTO
     * @return the corresponding {@link Payment} entity
     */
    public static Payment toEntity(PaymentRequestDTO dto) {
        log.debug("Mapping PaymentRequestDTO to Payment entity for orderId: {}", dto.getOrderId());
        return Payment.builder()
                .orderId(dto.getOrderId())
                .amount(dto.getAmount())
                .method(dto.getPaymentMethod()) // set payment method
                .status(null)                   // set during processing
                .timestamp(null)                // set during save
                .build();
    }

    /**
     * Converts a {@link Payment} entity to a {@link PaymentResponseDTO}.
     *
     * @param payment the {@link Payment} entity
     * @return the corresponding {@link PaymentResponseDTO}
     */
    public static PaymentResponseDTO toDTO(Payment payment) {
        log.debug("Mapping Payment entity to PaymentResponseDTO for paymentId: {}", payment.getId());
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod()) // include payment method in response
                .timestamp(payment.getTimestamp())
                .build();
    }
}

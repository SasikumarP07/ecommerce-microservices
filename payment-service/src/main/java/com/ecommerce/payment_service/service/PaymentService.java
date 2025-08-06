package com.ecommerce.payment_service.service;

import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;

/**
 * Service interface for handling payment operations.
 * <p>
 * Defines the contract for processing payments and retrieving payment details.
 * </p>
 */
public interface PaymentService {

    /**
     * Processes a new payment request.
     *
     * @param requestDTO the payment request containing order ID, amount, and method
     * @return the response containing payment details including status and timestamp
     */
    PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO);

    /**
     * Retrieves the payment details for the given order ID.
     *
     * @param orderId the ID of the order
     * @return the payment information associated with the order
     */
    PaymentResponseDTO getPaymentByOrderId(Long orderId);
}

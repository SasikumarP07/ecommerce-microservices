package com.ecommerce.payment_service.controller;

import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import com.ecommerce.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling payment-related operations.
 */
@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Endpoint to initiate a new payment.
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> makePayment(@RequestBody PaymentRequestDTO requestDTO) {
        log.info("Received payment request for orderId: {}", requestDTO.getOrderId());
        PaymentResponseDTO responseDTO = paymentService.processPayment(requestDTO);
        log.info("Payment processed successfully for orderId: {}", requestDTO.getOrderId());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve payment details by order ID.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        log.info("Fetching payment details for orderId: {}", orderId);
        PaymentResponseDTO responseDTO = paymentService.getPaymentByOrderId(orderId);
        log.info("Payment details retrieved for orderId: {}", orderId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}

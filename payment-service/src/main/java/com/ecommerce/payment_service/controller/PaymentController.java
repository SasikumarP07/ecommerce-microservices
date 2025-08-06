package com.ecommerce.payment_service.controller;

import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import com.ecommerce.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Payment Controller", description = "Handles payment operations like initiating and retrieving payments.")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Endpoint to initiate a new payment.
     */
    @Operation(
            summary = "Make a Payment",
            description = "Initiates a payment for the provided order with details like amount and user info."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid payment request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> makePayment(
            @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment request data including order ID, amount, and user info",
                    required = true
            ) PaymentRequestDTO requestDTO) {
        log.info("💳 Received payment request for orderId: {}", requestDTO.getOrderId());
        PaymentResponseDTO responseDTO = paymentService.processPayment(requestDTO);
        log.info("✅ Payment processed successfully for orderId: {}", requestDTO.getOrderId());
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve payment details by order ID.
     */
    @Operation(
            summary = "Get Payment by Order ID",
            description = "Retrieves payment details for the specified order ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found for the given order ID")
    })
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDTO> getPaymentByOrderId(
            @Parameter(description = "ID of the order to retrieve payment details", required = true)
            @PathVariable Long orderId) {
        log.info("📄 Fetching payment details for orderId: {}", orderId);
        PaymentResponseDTO responseDTO = paymentService.getPaymentByOrderId(orderId);
        log.info("📦 Payment details retrieved for orderId: {}", orderId);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}

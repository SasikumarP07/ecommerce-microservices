package com.ecommerce.payment_service.integrationtest;

import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import com.ecommerce.payment_service.controller.PaymentController;
import com.ecommerce.payment_service.enums.PaymentMethod;
import com.ecommerce.payment_service.enums.PaymentStatus;
import com.ecommerce.payment_service.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test class for {@link PaymentController}.
 * <p>
 * Uses {@code @WebMvcTest} to test controller endpoints in isolation,
 * with mocked {@link PaymentService} and real HTTP request simulation via {@link MockMvc}.
 * </p>
 */
@WebMvcTest(PaymentController.class)
@Import(PaymentControllerTest.TestConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PaymentService paymentService;

    private PaymentRequestDTO requestDTO;
    private PaymentResponseDTO responseDTO;

    /**
     * Initializes mock DTOs before each test case.
     */
    @BeforeEach
    void setUp() {
        requestDTO = PaymentRequestDTO.builder()
                .orderId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        responseDTO = PaymentResponseDTO.builder()
                .id(1L)
                .orderId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .status(PaymentStatus.SUCCESS)
                .build();
    }

    /**
     * Tests the POST /payments endpoint for successful payment creation.
     * <p>
     * Validates that the response contains expected order ID and status.
     * </p>
     */
    @Test
    void testMakePayment() throws Exception {
        when(paymentService.processPayment(any(PaymentRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    /**
     * Tests the GET /payments/order/{orderId} endpoint.
     * <p>
     * Ensures the controller returns correct payment details for a given order ID.
     * </p>
     */
    @Test
    void testGetPaymentByOrderId() throws Exception {
        when(paymentService.getPaymentByOrderId(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/payments/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId", is(1)))
                .andExpect(jsonPath("$.status", is("SUCCESS")));
    }

    /**
     * Internal configuration class to provide mocked {@link PaymentService} bean
     * for injection into the test context.
     */
    @TestConfiguration
    static class TestConfig {
        @Bean
        public PaymentService paymentService() {
            return mock(PaymentService.class);
        }
    }
}

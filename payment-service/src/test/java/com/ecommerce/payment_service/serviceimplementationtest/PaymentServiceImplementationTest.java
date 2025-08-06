package com.ecommerce.payment_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import com.ecommerce.common_dto.enums.PaymentMethod;
import com.ecommerce.common_dto.enums.PaymentStatus;
import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.mapper.PaymentMapper;
import com.ecommerce.payment_service.repository.PaymentRepository;
import com.ecommerce.payment_service.serviceimplementation.PaymentServiceImplementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PaymentServiceImplementation}.
 * <p>
 * Tests core functionalities of the service layer including:
 * <ul>
 *     <li>Processing a payment</li>
 *     <li>Fetching payment by order ID</li>
 *     <li>Handling payment not found scenario</li>
 * </ul>
 * Dependencies are mocked using Mockito to ensure isolation of logic.
 */
class PaymentServiceImplementationTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImplementation paymentService;

    private AutoCloseable closeable;

    /**
     * Initializes Mockito annotations before each test.
     */
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the {@code processPayment()} method.
     * <p>
     * Verifies that a payment is saved with status SUCCESS and a non-null response is returned.
     * </p>
     */
    @Test
    void testProcessPayment() {
        // Arrange
        PaymentRequestDTO requestDTO = PaymentRequestDTO.builder()
                .orderId(1L)
                .amount(BigDecimal.valueOf(100.0))
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        Payment mockEntity = PaymentMapper.toEntity(requestDTO);
        mockEntity.setStatus(PaymentStatus.SUCCESS);
        mockEntity.setTimestamp(LocalDateTime.now());

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockEntity);

        // Act
        PaymentResponseDTO response = paymentService.processPayment(requestDTO);

        // Assert
        assertNotNull(response);
        assertEquals(requestDTO.getOrderId(), response.getOrderId());
        assertEquals(requestDTO.getAmount(), response.getAmount());
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    /**
     * Test the {@code getPaymentByOrderId()} method for a successful case.
     * <p>
     * Ensures correct response is returned for an existing order ID.
     * </p>
     */
    @Test
    void testGetPaymentByOrderId_success() {
        // Arrange
        Long orderId = 10L;

        Payment payment = Payment.builder()
                .id(1L)
                .orderId(orderId)
                .amount(BigDecimal.valueOf(200.00))
                .status(PaymentStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();

        when(paymentRepository.findByOrderId(orderId)).thenReturn(payment);

        // Act
        PaymentResponseDTO response = paymentService.getPaymentByOrderId(orderId);

        // Assert
        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(PaymentStatus.SUCCESS, response.getStatus());

        verify(paymentRepository, times(1)).findByOrderId(orderId);
    }

    /**
     * Test the {@code getPaymentByOrderId()} method when no payment is found.
     * <p>
     * Verifies that a {@link RuntimeException} is thrown and appropriate message is returned.
     * </p>
     */
    @Test
    void testGetPaymentByOrderId_notFound() {
        // Arrange
        Long orderId = 99L;
        when(paymentRepository.findByOrderId(orderId)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                paymentService.getPaymentByOrderId(orderId));

        assertEquals("Payment not found for orderId: " + orderId, exception.getMessage());
        verify(paymentRepository, times(1)).findByOrderId(orderId);
    }
}

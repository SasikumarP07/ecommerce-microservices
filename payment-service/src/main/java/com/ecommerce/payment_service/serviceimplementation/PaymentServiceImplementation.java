package com.ecommerce.payment_service.serviceimplementation;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.order.OrderResponseDTO;
import com.ecommerce.common_dto.dto.payment.PaymentRequestDTO;
import com.ecommerce.common_dto.dto.payment.PaymentResponseDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.common_dto.enums.PaymentStatus;
import com.ecommerce.payment_service.client.NotificationClient;
import com.ecommerce.payment_service.client.OrderClient;
import com.ecommerce.payment_service.client.UserClient;
import com.ecommerce.payment_service.entity.Payment;
import com.ecommerce.payment_service.mapper.PaymentMapper;
import com.ecommerce.payment_service.repository.PaymentRepository;
import com.ecommerce.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of the {@link PaymentService} interface.
 * <p>
 * Handles core business logic for processing payments, saving payment records,
 * fetching associated order and user details, sending notifications, and retrieving payment info.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final NotificationClient notificationClient;

    /**
     * Processes a payment for the given order.
     * <p>
     * - Maps the payment request DTO to a {@link Payment} entity
     * - Saves the payment record with {@code SUCCESS} status
     * - Fetches order and user details
     * - Sends a success notification to the user
     * - Returns a mapped {@link PaymentResponseDTO}
     * </p>
     *
     * @param requestDTO the incoming payment request
     * @return the response containing processed payment details
     */
    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {
        log.info("💳 Processing payment for orderId: {}", requestDTO.getOrderId());

        // Convert DTO to entity and prepare for saving
        Payment payment = PaymentMapper.toEntity(requestDTO);
        payment.setStatus(PaymentStatus.SUCCESS); // Assuming successful payment
        payment.setTimestamp(LocalDateTime.now());

        // Save payment to DB
        Payment savedPayment = paymentRepository.save(payment);
        log.info("✅ Payment saved with ID: {}", savedPayment.getId());

        // Fetch order details from Order Service
        OrderResponseDTO order = orderClient.getOrderById(requestDTO.getOrderId());
        log.info("📦 Retrieved order details for orderId: {}", order.getId());

        // Fetch user details from User Service
        UserResponseDTO user = userClient.getUserById(order.getUserId());
        log.info("👤 Retrieved user details for userId: {}", user.getId());

        // Build and send notification
        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setToEmail(user.getEmail());
        notification.setSubject("Payment Successful");
        notification.setMessage("Your payment for order ID " + order.getId() + " was successful.");
        notificationClient.sendNotification(notification);
        log.info("📧 Payment notification sent to email: {}", user.getEmail());

        // Convert and return response DTO
        return PaymentMapper.toDTO(savedPayment);
    }

    /**
     * Retrieves the payment details for a given order ID.
     *
     * @param orderId the ID of the order
     * @return the payment details as a {@link PaymentResponseDTO}
     * @throws RuntimeException if no payment is found for the given order ID
     */
    @Override
    public PaymentResponseDTO getPaymentByOrderId(Long orderId) {
        log.info("🔍 Fetching payment details for orderId: {}", orderId);
        Payment payment = paymentRepository.findByOrderId(orderId);
        if (payment == null) {
            log.warn("❌ Payment not found for orderId: {}", orderId);
            throw new RuntimeException("Payment not found for orderId: " + orderId);
        }
        log.info("✅ Payment found with ID: {}", payment.getId());
        return PaymentMapper.toDTO(payment);
    }
}

package com.ecommerce.notification_service.service;

import com.ecommerce.common_dto.dto.notification.*;

/**
 * Service interface for handling notification operations.
 *
 * <p>This interface defines the contract for sending notifications.</p>
 *
 * @author YourName
 */
public interface NotificationService {

    /**
     * Sends a notification to the specified recipient.
     *
     * @param requestDTO the request object containing recipient email, subject, and message
     * @return a response object containing notification status and details
     */
    NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO);
}

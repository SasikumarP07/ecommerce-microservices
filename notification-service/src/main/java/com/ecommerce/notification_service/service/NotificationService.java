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
     * Sends a notification: saves to DB and sends email asynchronously.
     *
     * @param requestDTO the request object containing email, subject, and message
     * @return response DTO with saved notification info
     */
    NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO);
}

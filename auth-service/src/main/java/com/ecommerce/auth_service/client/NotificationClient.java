package com.ecommerce.auth_service.client;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Feign client interface for communicating with the Notification Service.
 * This client allows the Auth Service to send email or other notifications
 * by making HTTP requests to the Notification Service using declarative REST client style.
 */
@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    /**
     * Sends a notification using the Notification Service.
     *
     * @param request the notification details including recipient, subject, and message content
     */
    @PostMapping("api/notifications/send")
    void sendNotification(NotificationRequestDTO request);
}

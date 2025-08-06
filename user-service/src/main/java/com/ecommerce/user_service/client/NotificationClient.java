package com.ecommerce.user_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for communicating with the Notification Service.
 * <p>
 * This client handles sending notifications, such as welcome emails,
 * by making REST calls to the notification-service.
 */
@FeignClient(name = "notification-service")
public interface NotificationClient {

    /**
     * Sends a welcome email to the specified recipient.
     *
     * @param toEmail The email address of the recipient.
     */
    @PostMapping("/api/notification/send-welcome")
    void sendWelcomeEmail(@RequestParam("toEmail") String toEmail);
}

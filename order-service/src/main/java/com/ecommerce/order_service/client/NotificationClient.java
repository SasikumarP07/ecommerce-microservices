package com.ecommerce.order_service.client;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for communicating with the Notification Service.
 * <p>
 * This interface provides a method to send notifications by making a REST call
 * to the Notification Service's notification endpoint.
 */
@FeignClient(name = "notification-service")
public interface NotificationClient {

    /**
     * Sends a notification request to the Notification Service.
     *
     * @param requestDTO The notification request containing recipient, subject, and message content.
     */
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequestDTO requestDTO);
}

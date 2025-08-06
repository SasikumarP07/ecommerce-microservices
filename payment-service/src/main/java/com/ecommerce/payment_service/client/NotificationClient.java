package com.ecommerce.payment_service.client;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client for communicating with the Notification Service.
 * <p>
 * This interface is used by the Payment Service to send notifications
 * (e.g., payment success or failure messages) to users via the Notification Service.
 * </p>
 */
@FeignClient(name = "notification-service")
public interface NotificationClient {

    /**
     * Sends a notification using the Notification Service.
     *
     * @param request the notification request payload containing details like receiver, message, subject, etc.
     */
    @PostMapping("api/notifications/send")
    void sendNotification(@RequestBody NotificationRequestDTO request);
}

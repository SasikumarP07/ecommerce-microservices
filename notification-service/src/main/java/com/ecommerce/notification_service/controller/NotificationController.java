package com.ecommerce.notification_service.controller;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.notification_service.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Sends a generic notification email
     */
    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@Valid @RequestBody NotificationRequestDTO dto) {
        log.info("Sending notification to: {}", dto.getToEmail());
        notificationService.sendNotification(dto);
        return ResponseEntity.ok("Notification triggered successfully for " + dto.getToEmail());
    }

    /**
     * Sends a welcome email with Resilience4j protection
     */
    @PostMapping("/send-welcome")
    @CircuitBreaker(name = "notificationServiceCB", fallbackMethod = "sendWelcomeFallback")
    @Retry(name = "notificationServiceRetry")
    public ResponseEntity<String> sendWelcomeEmail(@RequestParam String toEmail) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setToEmail(toEmail);
        dto.setSubject("Welcome to Our Store!");
        dto.setMessage("Thanks for registering with us.");

        notificationService.sendNotification(dto);

        return ResponseEntity.ok("Welcome email triggered successfully for " + toEmail);
    }

    /**
     * Fallback method if NotificationService fails
     */
    public ResponseEntity<String> sendWelcomeFallback(String toEmail, Throwable ex) {
        log.error("Failed to send welcome email to {}. Reason: {}", toEmail, ex.getMessage());
        return ResponseEntity.status(503)
                .body("Notification service unavailable. Will retry sending welcome email for " + toEmail);
    }
}

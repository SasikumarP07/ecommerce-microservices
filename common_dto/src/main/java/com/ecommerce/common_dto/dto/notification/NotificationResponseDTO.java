package com.ecommerce.common_dto.dto.notification;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO representing the response after sending a notification.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponseDTO {

    /**
     * Unique identifier of the notification.
     */
    private Long id;

    /**
     * Recipient email address.
     */
    private String toEmail;

    /**
     * Subject of the email.
     */
    private String subject;

    /**
     * Body content of the email.
     */
    private String message;

    /**
     * Indicates whether the notification was successfully sent.
     */
    private boolean sent;

    /**
     * Timestamp when the notification was sent.
     */
    private LocalDateTime sentAt;
}

package com.ecommerce.notification_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a notification that is sent to a user via email.
 * Stores information such as recipient email, subject, message,
 * status of whether the email was sent, and the timestamp.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    /**
     * Unique identifier for the notification.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Recipient's email address.
     */
    private String toEmail;

    /**
     * Subject of the email.
     */
    private String subject;

    /**
     * Message body of the email. Limited to 1000 characters.
     */
    @Column(length = 1000)
    private String message;

    /**
     * Flag indicating whether the notification was successfully sent.
     */
    private boolean sent;

    /**
     * Timestamp indicating when the notification was sent.
     */
    private LocalDateTime sentAt;

    private String status;
}

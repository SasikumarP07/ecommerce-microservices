package com.ecommerce.common_dto.dto.notification;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO used to send email notification requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {

    /**
     * Recipient's email address.
     */
    @NotBlank(message = "Recipient email must not be blank")
    @Email(message = "Recipient email should be valid")
    private String toEmail;

    /**
     * Email subject line.
     */
    @NotBlank(message = "Subject must not be blank")
    private String subject;

    /**
     * Email message content.
     */
    @NotBlank(message = "Message must not be blank")
    private String message;
}

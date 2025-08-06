package com.ecommerce.notification_service.mapper;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.notification.NotificationResponseDTO;
import com.ecommerce.notification_service.entity.Notification;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Mapper class responsible for converting between
 * Notification entities and DTOs.
 */
@Slf4j
public class NotificationMapper {

    /**
     * Converts a NotificationRequestDTO to a Notification entity.
     * <p>
     * This is typically used when receiving a request to send a notification.
     * Sets 'sent' to false and 'sentAt' to current timestamp.
     *
     * @param dto the DTO containing the notification request data
     * @return a Notification entity with mapped data
     */
    public static Notification toEntity(NotificationRequestDTO dto) {
        log.debug("🔄 Converting NotificationRequestDTO to Notification entity: {}", dto);
        Notification notification = Notification.builder()
                .toEmail(dto.getToEmail())
                .subject(dto.getSubject())
                .message(dto.getMessage())
                .sent(false) // initially not sent
                .sentAt(LocalDateTime.now())
                .build();
        log.debug("✅ Created Notification entity: {}", notification);
        return notification;
    }

    /**
     * Converts a Notification entity to a NotificationResponseDTO.
     * <p>
     * This is used when sending the notification details back to the client.
     *
     * @param notification the entity containing persisted notification data
     * @return a response DTO containing mapped data
     */
    public static NotificationResponseDTO toDto(Notification notification) {
        log.debug("🔄 Converting Notification entity to NotificationResponseDTO: {}", notification);
        NotificationResponseDTO responseDTO = NotificationResponseDTO.builder()
                .id(notification.getId())
                .toEmail(notification.getToEmail())
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .sent(notification.isSent())
                .sentAt(notification.getSentAt())
                .build();
        log.debug("✅ Created NotificationResponseDTO: {}", responseDTO);
        return responseDTO;
    }
}

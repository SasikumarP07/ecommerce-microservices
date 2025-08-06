package com.ecommerce.notification_service.serviceimplementation;

import com.ecommerce.common_dto.dto.notification.*;
import com.ecommerce.notification_service.entity.Notification;
import com.ecommerce.notification_service.repository.NotificationRepository;
import com.ecommerce.notification_service.service.NotificationService;
import com.ecommerce.notification_service.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link NotificationService} for handling notification logic.
 *
 * <p>This class provides functionality to send notifications by storing them
 * in the database and returning a response DTO.</p>
 *
 * <p>Uses {@link NotificationMapper} to convert between DTO and entity objects.</p>
 *
 * <p>Logging is used to trace notification sending and persistence.</p>
 *
 * @author YourName
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Sends a notification by saving it to the database.
     *
     * @param requestDTO the request containing email, subject, and message
     * @return the response DTO containing saved notification details
     */
    @Override
    public NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO) {
        log.info("📩 Sending notification to email: {}", requestDTO.getToEmail());

        Notification notification = NotificationMapper.toEntity(requestDTO);
        Notification savedNotification = notificationRepository.save(notification);

        log.info("✅ Notification saved with ID: {}", savedNotification.getId());

        return NotificationMapper.toDto(savedNotification);
    }
}

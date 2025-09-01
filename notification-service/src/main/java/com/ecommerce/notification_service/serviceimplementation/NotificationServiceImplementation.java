package com.ecommerce.notification_service.serviceimplementation;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.notification.NotificationResponseDTO;
import com.ecommerce.notification_service.entity.Notification;
import com.ecommerce.notification_service.mapper.NotificationMapper;
import com.ecommerce.notification_service.repository.NotificationRepository;
import com.ecommerce.notification_service.service.NotificationService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImplementation implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    @CircuitBreaker(name = "notificationServiceCB", fallbackMethod = "sendNotificationFallback")
    @Retry(name = "notificationServiceRetry")
    public NotificationResponseDTO sendNotification(NotificationRequestDTO requestDTO) {
        log.info("Sending notification to email: {}", requestDTO.getToEmail());

        Notification notification = NotificationMapper.toEntity(requestDTO);
        Notification savedNotification = notificationRepository.save(notification);
        log.info("Notification saved with ID: {}", savedNotification.getId());

        sendEmailAsync(requestDTO);

        return NotificationMapper.toDto(savedNotification);
    }

    @Async
    private void sendEmailAsync(NotificationRequestDTO requestDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(requestDTO.getToEmail());
            message.setSubject(requestDTO.getSubject());
            message.setText(requestDTO.getMessage());

            mailSender.send(message);
            log.info("Email sent successfully to {}", requestDTO.getToEmail());
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", requestDTO.getToEmail(), e.getMessage(), e);
        }
    }

    public NotificationResponseDTO sendNotificationFallback(NotificationRequestDTO requestDTO, Throwable ex) {
        log.error("Notification service failed for email {}. Reason: {}", requestDTO.getToEmail(), ex.getMessage());

        Notification failedNotification = new Notification();
        failedNotification.setToEmail(requestDTO.getToEmail());
        failedNotification.setSubject(requestDTO.getSubject());
        failedNotification.setMessage(requestDTO.getMessage());
        failedNotification.setStatus("FAILED");

        Notification savedFailed = notificationRepository.save(failedNotification);

        return NotificationMapper.toDto(savedFailed);
    }
}

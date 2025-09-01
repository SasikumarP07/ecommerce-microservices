package com.ecommerce.notification_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.notification.NotificationResponseDTO;
import com.ecommerce.notification_service.entity.Notification;
import com.ecommerce.notification_service.mapper.NotificationMapper;
import com.ecommerce.notification_service.repository.NotificationRepository;
import com.ecommerce.notification_service.serviceimplementation.NotificationServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link NotificationServiceImplementation}.
 * <p>
 * This class uses JUnit 5 and Mockito to test the core functionality of sending a notification.
 * It ensures that a notification is properly saved in the database and the response is correctly mapped and returned.
 */
class NotificationServiceImplementationTest {

    /**
     * Mock of the {@link NotificationRepository} to simulate database operations.
     */
    @Mock
    private NotificationRepository notificationRepository;

    /**
     * The service under test, injected with mocked dependencies.
     */
    @InjectMocks
    private NotificationServiceImplementation notificationService;

    /**
     * Initializes Mockito annotations before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for {@link NotificationServiceImplementation#sendNotification(NotificationRequestDTO)}.
     * <p>
     * This test verifies:
     * <ul>
     *     <li>The notification is saved to the repository.</li>
     *     <li>The response contains correct values as per the saved notification.</li>
     *     <li>The save method is called exactly once.</li>
     * </ul>
     */
    @Test
    void sendNotification_shouldReturnResponseDTO() {
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setToEmail("user@example.com");
        requestDTO.setSubject("Test Subject");
        requestDTO.setMessage("Test Message");

        Notification notificationEntity = NotificationMapper.toEntity(requestDTO);
        notificationEntity.setId(1L);

        Notification savedNotification = new Notification();
        savedNotification.setId(1L);
        savedNotification.setToEmail("user@example.com");
        savedNotification.setSubject("Test Subject");
        savedNotification.setMessage("Test Message");

        when(notificationRepository.save(any(Notification.class))).thenReturn(savedNotification);

        NotificationResponseDTO responseDTO = notificationService.sendNotification(requestDTO);

        assertNotNull(responseDTO, "Response should not be null");
        assertEquals(1L, responseDTO.getId());
        assertEquals("user@example.com", responseDTO.getToEmail());
        assertEquals("Test Subject", responseDTO.getSubject());
        assertEquals("Test Message", responseDTO.getMessage());

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}

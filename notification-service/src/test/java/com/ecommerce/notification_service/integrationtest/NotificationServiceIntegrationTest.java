package com.ecommerce.notification_service.integrationtest;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.notification_service.repository.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for the Notification Service.
 * This test verifies that sending a notification through the API
 * saves the notification correctly and returns the expected response.
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case for sending a notification.
     * Scenario:
     * - Given a valid {@link NotificationRequestDTO}
     * - When a POST request is made to the /api/notifications endpoint
     * - Then the response should return HTTP 201 Created and the persisted notification details
     *
     * @throws Exception if the mockMvc request fails
     */
    @Test
    void sendNotification_shouldSaveAndReturnNotification() throws Exception {
        // Given
        NotificationRequestDTO requestDTO = new NotificationRequestDTO();
        requestDTO.setToEmail("test@example.com");
        requestDTO.setSubject("Integration Test");
        requestDTO.setMessage("This is a test notification");

        // When & Then
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.toEmail").value("test@example.com"))
                .andExpect(jsonPath("$.subject").value("Integration Test"))
                .andExpect(jsonPath("$.message").value("This is a test notification"));
    }
}

package com.ecommerce.notification_service.controller;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.notification.NotificationResponseDTO;
import com.ecommerce.notification_service.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling notification-related operations.
 * Provides endpoints to send general and welcome notifications.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Sends a generic notification email using the provided request data.
     *
     * @param requestDTO the notification request details (toEmail, subject, message)
     * @return the response with email status and details
     */
    @Operation(
            summary = "Send a notification email",
            description = "Send an email notification to the specified user email address.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Notification details including email, subject, and message",
                    required = true,
                    content = @Content(schema = @Schema(implementation = NotificationRequestDTO.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification sent successfully",
                    content = @Content(schema = @Schema(implementation = NotificationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/send")
    public ResponseEntity<NotificationResponseDTO> sendNotification(
            @Valid @RequestBody NotificationRequestDTO requestDTO) {
        log.info("📩 Sending notification to: {}", requestDTO.getToEmail());
        NotificationResponseDTO responseDTO = notificationService.sendNotification(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    /**
     * Sends a predefined welcome email to the newly registered user.
     *
     * @param toEmail the recipient's email address
     */
    @Operation(
            summary = "Send welcome email",
            description = "Send a predefined welcome email to a newly registered user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcome email sent"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/send-welcome")
    public void sendWelcomeEmail(@RequestParam("toEmail") String toEmail) {
        NotificationRequestDTO dto = new NotificationRequestDTO();
        dto.setToEmail(toEmail);
        dto.setSubject("Welcome to Our Store!");
        dto.setMessage("Thanks for registering with us.");
        notificationService.sendNotification(dto);
    }
}

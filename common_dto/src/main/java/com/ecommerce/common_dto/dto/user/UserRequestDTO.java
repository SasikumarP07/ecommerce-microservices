package com.ecommerce.common_dto.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * DTO for capturing user registration or update request data.
 * Includes basic validations to ensure data integrity.
 */
@Data
public class UserRequestDTO {

    // User's full name, required to be between 2 and 50 characters.
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    // Phone number must be exactly 10 digits (India-specific format).
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    // Valid email address is required.
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    // Password must be at least 6 characters long.
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}

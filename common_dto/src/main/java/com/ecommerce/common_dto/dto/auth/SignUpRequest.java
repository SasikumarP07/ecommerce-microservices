package com.ecommerce.common_dto.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * It includes all necessary fields required for user sign-up,
 * along with validation constraints to enforce data integrity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    /**
     * The email address of the user.
     * Must not be blank and must follow a valid email format.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password for the new user.
     * Must not be blank and should be at least 6 characters long.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    /**
     * The full name of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Name must not be blank")
    private String name;

    /**
     * The phone number of the user.
     * Must be exactly 10 digits.
     */
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    /**
     * The address of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Address must not be blank")
    private String address;
}

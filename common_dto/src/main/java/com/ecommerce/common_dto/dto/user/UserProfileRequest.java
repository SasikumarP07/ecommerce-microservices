package com.ecommerce.common_dto.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Data Transfer Object for receiving user profile data in create or update requests.
 * Includes validation annotations to ensure input integrity.
 */
@Data
@NoArgsConstructor
@ToString
public class UserProfileRequest {

    /**
     * Unique identifier for the user.
     * Typically optional in request; used when updating an existing user.
     */
    private Long id;

    /**
     * Full name of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Name must not be blank")
    private String name;

    /**
     * Phone number of the user.
     * Must be exactly 10 digits.
     */
    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    /**
     * Address of the user.
     * Must not be blank.
     */
    @NotBlank(message = "Address must not be blank")
    private String address;

    /**
     * Email address of the user.
     * Must be valid and not blank.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    /**
     * Password for user authentication.
     * Must be at least 6 characters long and not blank.
     */
    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    /**
     * Role assigned to the user (e.g., USER, ADMIN).
     * Must not be blank.
     */
    @NotBlank(message = "Role must not be blank")
    private String role;
}

package com.ecommerce.common_dto.dto.auth;

import com.ecommerce.common_dto.dto.user.AddressRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Supports multiple addresses for the user.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Phone number must not be blank")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    /**
     * List of addresses for the user.
     * At least one address must be provided.
     */
    private List<AddressRequest> addresses;

    /**
     * The role of the user (e.g., ROLE_USER, ROLE_ADMIN).
     * Defaults to ROLE_USER if not provided.
     */
    private String role;
}

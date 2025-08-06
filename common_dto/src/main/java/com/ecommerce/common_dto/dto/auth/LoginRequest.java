package com.ecommerce.common_dto.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object (DTO) for user login requests.
 * It contains the credentials required for authentication: email and password.
 * This class includes validation constraints to ensure valid input from the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginRequest {

    /**
     * The email address of the user attempting to log in.
     * Must not be blank and must follow a valid email format.
     */
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password corresponding to the user's account.
     * Must not be blank.
     */
    @NotBlank(message = "Password must not be blank")
    private String password;
}

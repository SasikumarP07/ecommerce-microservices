package com.ecommerce.auth_service.service;

import com.ecommerce.common_dto.dto.auth.LoginRequest;
import com.ecommerce.common_dto.dto.auth.SignUpRequest;

/**
 * AuthService defines the contract for authentication operations.
 * This includes user registration, login, and email existence check.
 */
public interface AuthService {

    /**
     * Registers a new user with the provided sign-up details.
     *
     * @param request The sign-up request containing user email, password, and role.
     * @return A success message or token if registration is successful.
     */
    String register(SignUpRequest request);

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to be checked.
     * @return true if a user with the email exists, false otherwise.
     */
    boolean existByEmail(String email);

    /**
     * Authenticates the user using provided login credentials.
     *
     * @param loginRequest The login request containing email and password.
     * @return A JWT token as a string if authentication is successful.
     */
    String login(LoginRequest loginRequest);
}

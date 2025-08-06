package com.ecommerce.auth_service.serviceImplementation;

import com.ecommerce.auth_service.client.NotificationClient;
import com.ecommerce.auth_service.client.UserClient;
import com.ecommerce.auth_service.entity.AuthUser;
import com.ecommerce.auth_service.repository.AuthUserRepository;
import com.ecommerce.auth_service.service.AuthService;
import com.ecommerce.common_dto.dto.auth.LoginRequest;
import com.ecommerce.common_dto.dto.auth.SignUpRequest;
import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import com.ecommerce.common_util.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the AuthService interface.
 * Handles user registration, login, and email existence checks.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImplementation implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserClient userClient;
    private final NotificationClient notificationClient;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user by:
     * 1. Saving user credentials in Auth DB
     * 2. Creating user profile in User Service via Feign client
     * 3. Sending a welcome email via Notification Service
     *
     * @param request The sign-up request data
     * @return JWT token for the registered user
     */
    @Override
    public String register(SignUpRequest request) {
        log.info("📝 Registering new user with email: {}", request.getEmail());

        // Save AuthUser
        AuthUser user = new AuthUser();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        AuthUser savedUser = authUserRepository.save(user);
        log.info("✅ AuthUser saved with ID: {}", savedUser.getId());

        // Create User Profile via Feign
        UserProfileRequest profile = new UserProfileRequest();
        profile.setId(savedUser.getId());
        profile.setName(request.getName());
        profile.setPhone(request.getPhone());
        profile.setAddress(request.getAddress());
        profile.setEmail(savedUser.getEmail());
        profile.setPassword(savedUser.getPassword());
        profile.setRole(savedUser.getRole());

        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getRole());
        log.info("🔐 JWT token generated for user ID: {}", savedUser.getId());

        userClient.createUser(profile, "Bearer " + token);
        log.info("📨 User profile sent to User Service via Feign Client");

        // Send Welcome Notification
        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setToEmail(savedUser.getEmail());
        notification.setSubject("Welcome to E-Commerce App");
        notification.setMessage("Hi " + request.getName() + ",\n\nThanks for registering!");

        notificationClient.sendNotification(notification);
        log.info("📧 Welcome notification sent to email: {}", savedUser.getEmail());

        return token;
    }

    /**
     * Checks if a user exists with the given email.
     *
     * @param email The email to check
     * @return true if email exists, false otherwise
     */
    @Override
    public boolean existByEmail(String email) {
        boolean exists = authUserRepository.existsByEmail(email);
        log.debug("🔍 Checking if email exists ({}): {}", email, exists);
        return exists;
    }

    /**
     * Authenticates user credentials and returns JWT token upon success.
     *
     * @param loginRequest Contains email and password
     * @return JWT token for authenticated user
     */
    @Override
    public String login(LoginRequest loginRequest) {
        log.info("🔑 Login attempt for email: {}", loginRequest.getEmail());

        AuthUser user = authUserRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("❌ User not found for email: {}", loginRequest.getEmail());
                    return new UsernameNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("❌ Invalid credentials for email: {}", loginRequest.getEmail());
            throw new BadCredentialsException("Invalid credentials");
        }

        log.info("✅ Login successful for user ID: {}", user.getId());
        return jwtUtil.generateToken(user.getId(), user.getRole());
    }
}

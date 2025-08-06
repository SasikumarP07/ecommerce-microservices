package com.ecommerce.auth_service.serviceimplementationtest;

import com.ecommerce.auth_service.client.NotificationClient;
import com.ecommerce.auth_service.client.UserClient;
import com.ecommerce.auth_service.entity.AuthUser;
import com.ecommerce.auth_service.repository.AuthUserRepository;
import com.ecommerce.auth_service.serviceImplementation.AuthServiceImplementation;
import com.ecommerce.common_dto.dto.auth.LoginRequest;
import com.ecommerce.common_dto.dto.auth.SignUpRequest;
import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import com.ecommerce.common_util.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AuthServiceImplementation}.
 * This test class uses Mockito to mock dependencies and verify
 * the behavior of AuthServiceImplementation methods including
 * user registration, login, and email existence checks.
 */
class AuthServiceImplementationTest {

    @InjectMocks
    private AuthServiceImplementation authService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserClient userClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    /**
     * Initializes mocks before each test execution.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests successful user registration flow.
     * Verifies that password encoding, user saving, JWT generation,
     * user client call, and notification sending occur as expected.
     */
    @Test
    void testRegister_Success() {
        SignUpRequest request = new SignUpRequest("test@example.com", "password123", "John", "1234567890", "NYC");

        AuthUser savedUser = new AuthUser();
        savedUser.setId(1L);
        savedUser.setEmail(request.getEmail());
        savedUser.setPassword("encoded-password");
        savedUser.setRole("ROLE_USER");

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded-password");
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(1L, "ROLE_USER")).thenReturn("mocked-jwt-token");

        String result = authService.register(request);

        assertEquals("mocked-jwt-token", result);

        verify(userClient).createUser(any(UserProfileRequest.class), eq("Bearer mocked-jwt-token"));
        verify(notificationClient).sendNotification(any(NotificationRequestDTO.class));
    }

    /**
     * Tests the scenario where an email exists in the repository.
     * Expects existByEmail() to return true.
     */
    @Test
    void testExistByEmail_True() {
        when(authUserRepository.existsByEmail("exist@example.com")).thenReturn(true);
        assertTrue(authService.existByEmail("exist@example.com"));
    }

    /**
     * Tests the scenario where an email does not exist in the repository.
     * Expects existByEmail() to return false.
     */
    @Test
    void testExistByEmail_False() {
        when(authUserRepository.existsByEmail("notfound@example.com")).thenReturn(false);
        assertFalse(authService.existByEmail("notfound@example.com"));
    }

    /**
     * Tests successful login with valid email and password.
     * Verifies that the correct JWT token is returned.
     */
    @Test
    void testLogin_Success() {
        LoginRequest request = new LoginRequest("test@example.com", "correctPassword");

        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");

        when(authUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctPassword", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(user.getId(), user.getRole())).thenReturn("valid-jwt");

        String token = authService.login(request);
        assertEquals("valid-jwt", token);
    }

    /**
     * Tests login failure when user email is not found.
     * Expects a UsernameNotFoundException to be thrown.
     */
    @Test
    void testLogin_UserNotFound() {
        LoginRequest request = new LoginRequest("notfound@example.com", "password");
        when(authUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.login(request));
    }

    /**
     * Tests login failure when password does not match.
     * Expects a BadCredentialsException to be thrown.
     */
    @Test
    void testLogin_InvalidPassword() {
        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");

        AuthUser user = new AuthUser();
        user.setId(1L);
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");

        when(authUserRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }
}

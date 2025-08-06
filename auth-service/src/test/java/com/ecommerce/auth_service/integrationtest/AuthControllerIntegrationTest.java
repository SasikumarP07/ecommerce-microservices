package com.ecommerce.auth_service.integrationtest;

import com.ecommerce.auth_service.serviceImplementation.AuthServiceImplementation;
import com.ecommerce.common_dto.dto.auth.LoginRequest;
import com.ecommerce.common_dto.dto.auth.SignUpRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the AuthController.
 * This class uses MockMvc to test the complete request-response cycle
 * for authentication-related endpoints (`/auth/signup` and `/auth/login`).
 * All dependencies such as AuthServiceImplementation are mocked to isolate
 * controller logic from the service layer.
 */
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthServiceImplementation authService;

    @Autowired
    private ObjectMapper objectMapper;

    // ---------- SIGNUP TESTS ----------

    /**
     * Test the successful signup flow.
     * Verifies that a valid signup request returns a 200 OK with a JWT token.
     */

    @Test
    void testSignup_Success() throws Exception {
        SignUpRequest request = new SignUpRequest("test@example.com", "pass123", "John", "9999999999", "NYC");

        Mockito.when(authService.existByEmail("test@example.com")).thenReturn(false);
        Mockito.when(authService.register(any(SignUpRequest.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    /**
     * Test signup failure when email is already registered.
     * Verifies that the API returns a 400 Bad Request with appropriate error message.
     */

    @Test
    void testSignup_EmailAlreadyExists() throws Exception {
        SignUpRequest request = new SignUpRequest("existing@example.com", "pass123", "Jane", "8888888888", "LA");

        Mockito.when(authService.existByEmail("existing@example.com")).thenReturn(true);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email already registered")));
    }

    /**
     * Test internal server error during signup.
     * Simulates a RuntimeException and expects a 500 Internal Server Error.
     */

    @Test
    void testSignup_InternalServerError() throws Exception {
        SignUpRequest request = new SignUpRequest("test2@example.com", "pass123", "Rick", "7777777777", "CA");

        Mockito.when(authService.existByEmail("test2@example.com")).thenReturn(false);
        Mockito.when(authService.register(any(SignUpRequest.class))).thenThrow(new RuntimeException("DB error"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Signup failed")));
    }

    // ---------- LOGIN TESTS ----------

    /**
     * Test the successful login flow.
     * Verifies that valid credentials return a 200 OK with a JWT token.
     */

    @Test
    void testLogin_Success() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "correct");

        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    /**
     * Test login failure when user is not found.
     * Verifies that a UsernameNotFoundException results in a 401 Unauthorized response.
     */

    @Test
    void testLogin_UserNotFound() throws Exception {
        LoginRequest request = new LoginRequest("notfound@example.com", "pass");

        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenThrow(new UsernameNotFoundException("User not found"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("User not found")));
    }

    /**
     * Test login failure due to incorrect credentials.
     * Verifies that a BadCredentialsException results in a 401 Unauthorized response.
     */

    @Test
    void testLogin_BadCredentials() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "wrong");

        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid credentials")));
    }

    /**
     * Test unexpected server error during login.
     * Simulates a RuntimeException and expects a 500 Internal Server Error.
     */

    @Test
    void testLogin_InternalServerError() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "pass");

        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Something went wrong")));
    }
}


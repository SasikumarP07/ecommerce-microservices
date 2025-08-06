package com.ecommerce.auth_service.controller;

import com.ecommerce.auth_service.entity.JwtResponse;
import com.ecommerce.auth_service.serviceImplementation.AuthServiceImplementation;
import com.ecommerce.common_dto.dto.auth.LoginRequest;
import com.ecommerce.common_dto.dto.auth.SignUpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController handles user authentication-related operations such as
 * signup and login. It delegates business logic to the AuthServiceImplementation
 * and responds with appropriate HTTP status codes.
 * Endpoints:
 * - POST /auth/signup : Register a new user and return JWT token
 * - POST /auth/login  : Authenticate user and return JWT token
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthServiceImplementation authService;

    @Operation(
            summary = "Register a new user",
            description = "Registers a user and returns a JWT token on success."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Signup successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "400", description = "Email already registered"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
        log.info("➡️ Signup request received for email: {}", request.getEmail());

        if (authService.existByEmail(request.getEmail())) {
            log.warn("❌ Signup failed: Email '{}' is already registered", request.getEmail());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Email already registered");
        }

        try {
            String token = authService.register(request);
            log.info("✅ Signup successful for email: {}", request.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (Exception e) {
            log.error("🔥 Exception during signup for email {}: {}", request.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Signup failed");
        }
    }

    @Operation(
            summary = "Authenticate user and return JWT",
            description = "Validates credentials and returns a JWT token if successful."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = JwtResponse.class))),
            @ApiResponse(responseCode = "401", description = "User not found or invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Something went wrong")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        log.info("➡️ Login request received for username: {}", loginRequest.getEmail());

        try {
            String token = authService.login(loginRequest);
            log.info("✅ Login successful for username: {}", loginRequest.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (UsernameNotFoundException ex) {
            log.warn("❌ Login failed: User '{}' not found", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (BadCredentialsException ex) {
            log.warn("❌ Login failed: Invalid credentials for user '{}'", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (Exception ex) {
            log.error("🔥 Exception during login for user '{}': {}", loginRequest.getEmail(), ex.getMessage(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }
}

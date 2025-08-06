package com.ecommerce.user_service.controller;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.serviceimplementation.UserServiceImplementation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImplementation userService;

    @Operation(
            summary = "Create a new user profile",
            description = "Creates a new user and stores the profile information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserProfileRequest userProfileRequest,  // Only use Spring's annotation

            @Parameter(description = "Authorization token")
            @RequestHeader("Authorization") String token
    ) {
        log.info("Create user request received with token");
        UserResponseDTO userResponseDTO = userService.save(UserMapper.toEntity(userProfileRequest));
        return ResponseEntity.ok(userResponseDTO);
    }


    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        log.info("Fetching user by ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Update user by ID", description = "Updates a user's profile information")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDTO dto
    ) {
        log.info("Updating user with ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @Operation(summary = "Get addresses for a user", description = "Fetches all saved addresses for a user")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@PathVariable Long id) {
        log.info("Fetching addresses for user ID: {}", id);
        return ResponseEntity.ok(userService.getAddresses(id));
    }

    @Operation(summary = "Add address for a user", description = "Adds a new address to the user profile")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable Long id,
            @RequestBody @Valid AddressRequestDTO dto
    ) {
        log.info("Adding address for user ID: {}", id);
        return ResponseEntity.ok(userService.addAddress(id, dto));
    }

    @Operation(summary = "Update an address for a user", description = "Updates an existing address of a user")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @PathVariable Long id,
            @PathVariable Long addressId,
            @RequestBody AddressRequestDTO dto
    ) {
        log.info("Updating address ID: {} for user ID: {}", addressId, id);
        return ResponseEntity.ok(userService.updateAddress(id, addressId, dto));
    }

    @Operation(summary = "Delete an address for a user", description = "Deletes a user's address by ID")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long id,
            @PathVariable Long addressId
    ) {
        log.info("Deleting address ID: {} for user ID: {}", addressId, id);
        userService.deleteAddress(id, addressId);
        return ResponseEntity.noContent().build();
    }
}

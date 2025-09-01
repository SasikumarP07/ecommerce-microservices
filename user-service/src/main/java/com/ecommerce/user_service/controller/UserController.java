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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserServiceImplementation userService;

    @PostMapping("/save")
    public ResponseEntity<UserResponseDTO> createUser(
            @RequestBody UserProfileRequest userProfileRequest,
            @RequestHeader("Authorization") String token
    ) {
        log.info("Create user request received with token");
        System.out.println(userProfileRequest);
        UserResponseDTO userResponseDTO = userService.save(UserMapper.toEntity(userProfileRequest));
        return ResponseEntity.ok(userResponseDTO);
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) {
        log.info("Fetching user by ID: {}", id);
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @RequestBody @Valid UserRequestDTO dto
    ) {
        log.info("Updating user with ID: {}", id);
        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@PathVariable Long id) {
        log.info("Fetching addresses for user ID: {}", id);
        return ResponseEntity.ok(userService.getAddresses(id));
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressResponseDTO> addAddress(
            @PathVariable Long id,
            @RequestBody @Valid AddressRequestDTO dto
    ) {
        log.info("Adding address for user ID: {}", id);
        return ResponseEntity.ok(userService.addAddress(id, dto));
    }

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

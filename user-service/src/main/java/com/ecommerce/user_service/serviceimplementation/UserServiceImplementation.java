package com.ecommerce.user_service.serviceimplementation;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.user_service.client.NotificationClient;
import com.ecommerce.user_service.entity.Address;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.exception.ResourceNotFoundException;
import com.ecommerce.user_service.exception.UnauthorizedAccessException;
import com.ecommerce.user_service.mapper.AddressMapper;
import com.ecommerce.user_service.mapper.UserMapper;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.service.AddressService;
import com.ecommerce.user_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of the {@link UserService} interface.
 * This class handles the business logic related to user management, including CRUD operations
 * for users and their associated addresses.
 * It also integrates with the {@link NotificationClient} to send notifications.
 * All methods are transactional where necessary.
 *
 * @author
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService {

    private final UserRepository repository;
    private final AddressService addressService;
    private final NotificationClient notificationClient;

    /**
     * Saves a new user and sends a welcome email.
     *
     * @param user the user entity to be saved
     * @return UserResponseDTO with the saved user details
     */
    @Override
    @Transactional
    public UserResponseDTO save(User user) {
        log.info("📥 Saving user: {}", user);
        try {
            User savedUser = repository.save(user);
            log.debug("✅ User saved successfully with ID: {}", savedUser.getId());

            try {
                notificationClient.sendWelcomeEmail(savedUser.getEmail());
                log.info("📧 Welcome email sent to: {}", savedUser.getEmail());
            } catch (Exception e) {
                log.error("❌ Failed to send welcome email to: {}", savedUser.getEmail(), e);
            }

            return UserMapper.toDTO(savedUser);
        } catch (Exception e) {
            log.error("❌ Exception occurred while saving user", e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    /**
     * Retrieves user details by ID.
     *
     * @param id the user ID
     * @return UserResponseDTO containing user data
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        log.info("🔍 Fetching user with ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });
        return UserMapper.toDTO(user);
    }

    /**
     * Updates an existing user.
     *
     * @param id  the user ID
     * @param dto the updated user information
     * @return updated UserResponseDTO
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        log.info("✏️ Updating user with ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        User updatedUser = repository.save(UserMapper.toEntity(dto, user));
        log.debug("✅ User updated: {}", updatedUser);
        return UserMapper.toDTO(updatedUser);
    }

    /**
     * Retrieves all addresses associated with a user.
     *
     * @param id the user ID
     * @return list of AddressResponseDTO
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<AddressResponseDTO> getAddresses(Long id) {
        log.info("📦 Fetching addresses for user ID: {}", id);
        User user = repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        List<AddressResponseDTO> addresses = AddressMapper.toDTO(user);
        log.debug("✅ Found {} addresses for user ID: {}", addresses.size(), id);
        return addresses;
    }

    /**
     * Adds a new address for a user.
     *
     * @param id  the user ID
     * @param dto address request data
     * @return saved AddressResponseDTO
     * @throws ResourceNotFoundException if user does not exist
     */
    @Override
    @Transactional
    public AddressResponseDTO addAddress(Long id, AddressRequestDTO dto) {
        log.info("➕ Adding address for user ID: {}", id);
        repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        Address address = AddressMapper.toEntity(dto);
        Address savedAddress = addressService.save(address);
        log.debug("✅ Address added: {}", savedAddress);
        return AddressMapper.toDTO(savedAddress);
    }

    /**
     * Updates an existing address for a user.
     *
     * @param id        the user ID
     * @param addressId the address ID
     * @param dto       updated address data
     * @return updated AddressResponseDTO
     * @throws ResourceNotFoundException   if user or address does not exist
     * @throws UnauthorizedAccessException if address does not belong to the user
     */
    @Override
    @Transactional
    public AddressResponseDTO updateAddress(Long id, Long addressId, AddressRequestDTO dto) {
        log.info("✏️ Updating address ID: {} for user ID: {}", addressId, id);

        repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        Address address = addressService.getAddressId(addressId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with ID: " + addressId);
                });

        if (!address.getUser().getId().equals(id)) {
            log.warn("🚫 Unauthorized access: Address {} does not belong to user {}", addressId, id);
            throw new UnauthorizedAccessException("Address does not belong to the specified user");
        }

        Address updatedAddress = addressService.save(AddressMapper.toEntity(address, dto));
        log.debug("✅ Address updated: {}", updatedAddress);
        return AddressMapper.toDTO(updatedAddress);
    }

    /**
     * Deletes an address for a user.
     *
     * @param id        the user ID
     * @param addressId the address ID
     * @return deleted AddressResponseDTO
     * @throws ResourceNotFoundException   if user or address does not exist
     * @throws UnauthorizedAccessException if address does not belong to the user
     */
    @Override
    @Transactional
    public AddressResponseDTO deleteAddress(Long id, Long addressId) {
        log.info("🗑️ Deleting address ID: {} for user ID: {}", addressId, id);

        repository.findById(id)
                .orElseThrow(() -> {
                    log.warn("⚠️ User not found with ID: {}", id);
                    return new ResourceNotFoundException("User not found with ID: " + id);
                });

        Address address = addressService.getAddressId(addressId)
                .orElseThrow(() -> {
                    log.warn("⚠️ Address not found with ID: {}", addressId);
                    return new ResourceNotFoundException("Address not found with ID: " + addressId);
                });

        if (!address.getUser().getId().equals(id)) {
            log.warn("🚫 Unauthorized deletion attempt: Address {} does not belong to user {}", addressId, id);
            throw new UnauthorizedAccessException("Address does not belong to the specified user");
        }

        addressService.delete(address.getId());
        log.debug("✅ Address deleted: {}", addressId);
        return AddressMapper.toDTO(address);
    }
}

package com.ecommerce.user_service.service;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.user_service.entity.User;

import java.util.List;

/**
 * Service interface for managing user operations such as registration,
 * profile updates, and address management.
 */
public interface UserService {

    /**
     * Saves a new user to the database.
     *
     * @param user the User entity to save
     * @return the saved user's details as a {@link UserResponseDTO}
     */
    UserResponseDTO save(User user);

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id the ID of the user
     * @return the user's details as a {@link UserResponseDTO}
     */
    UserResponseDTO getUserById(Long id);

    /**
     * Updates the user's profile information.
     *
     * @param id  the ID of the user to update
     * @param dto the new user data as a {@link UserRequestDTO}
     * @return the updated user's details as a {@link UserResponseDTO}
     */
    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    /**
     * Retrieves all addresses associated with a user.
     *
     * @param id the ID of the user
     * @return a list of {@link AddressResponseDTO} associated with the user
     */
    List<AddressResponseDTO> getAddresses(Long id);

    /**
     * Adds a new address to the user's address list.
     *
     * @param id  the ID of the user
     * @param dto the address details as a {@link AddressRequestDTO}
     * @return the added address as a {@link AddressResponseDTO}
     */
    AddressResponseDTO addAddress(Long id, AddressRequestDTO dto);

    /**
     * Updates an existing address for a user.
     *
     * @param id        the ID of the user
     * @param addressId the ID of the address to update
     * @param dto       the new address details as a {@link AddressRequestDTO}
     * @return the updated address as a {@link AddressResponseDTO}
     */
    AddressResponseDTO updateAddress(Long id, Long addressId, AddressRequestDTO dto);

    /**
     * Deletes an address associated with the user.
     *
     * @param id        the ID of the user
     * @param addressId the ID of the address to delete
     * @return the deleted address as a {@link AddressResponseDTO}
     */
    AddressResponseDTO deleteAddress(Long id, Long addressId);
}

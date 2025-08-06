package com.ecommerce.user_service.mapper;

import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.user_service.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Mapper class for converting between User entity and DTOs.
 */
@Slf4j
public class UserMapper {

    /**
     * Updates an existing {@link User} entity with the values from {@link UserRequestDTO}.
     *
     * @param dto  the data transfer object containing user information.
     * @param user the existing User entity to be updated.
     * @return the updated User entity.
     */
    public static User toEntity(UserRequestDTO dto, User user) {
        log.info("🔄 Mapping UserRequestDTO to existing User entity: {}", dto);
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }

    /**
     * Converts a {@link UserProfileRequest} to a new {@link User} entity.
     *
     * @param userProfileRequest the profile request data.
     * @return a new User entity with the provided data.
     */
    public static User toEntity(UserProfileRequest userProfileRequest) {
        log.info("🛠️ Mapping UserProfileRequest to new User entity: {}", userProfileRequest);
        User user = new User();
        user.setId(userProfileRequest.getId());
        user.setName(userProfileRequest.getName());
        user.setPhone(userProfileRequest.getPhone());
        user.setEmail(userProfileRequest.getEmail());
        user.setPassword(userProfileRequest.getPassword());
        return user;
    }

    /**
     * Converts a {@link User} entity to a {@link UserResponseDTO}.
     *
     * @param user the User entity.
     * @return a UserResponseDTO with mapped user data.
     */
    public static UserResponseDTO toDTO(User user) {
        log.info("📤 Mapping User entity to UserResponseDTO for userId: {}", user.getId());
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setAddresses(AddressMapper.toDTO(user));
        return dto;
    }
}

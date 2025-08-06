package com.ecommerce.common_dto.dto.user;

import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import lombok.Data;

import java.util.List;

/**
 * DTO for sending user details in responses, including associated addresses.
 */
@Data
public class UserResponseDTO {

    // Unique identifier for the user
    private Long id;

    // Full name of the user
    private String name;

    // Phone number of the user
    private String phone;

    // Email address of the user
    private String email;

    // List of addresses associated with the user
    private List<AddressResponseDTO> addresses;
}

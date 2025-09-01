package com.ecommerce.common_dto.dto.user;

import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import lombok.Data;

import java.util.List;

/**
 * DTO for sending user details in responses, including associated addresses.
 */
@Data
public class UserResponseDTO {

    private Long id;

    private String name;

    private String phone;

    private String email;

    private List<AddressResponseDTO> addresses;
}

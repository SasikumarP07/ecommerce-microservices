package com.ecommerce.common_dto.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) used to send address details back to the client
 * as part of API responses. It includes all fields required to represent
 * an address, along with its unique identifier.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDTO {

    /**
     * Unique identifier for the address.
     */
    private Long id;

    /**
     * Door number of the address.
     */
    private long doorNum;

    /**
     * Street name of the address.
     */
    private String street;

    /**
     * City in which the address is located.
     */
    private String city;

    /**
     * State in which the address is located.
     */
    private String state;

    /**
     * Pin code of the address.
     */
    private String pinCode;

    /**
     * Country in which the address is located.
     */
    private String country;
}

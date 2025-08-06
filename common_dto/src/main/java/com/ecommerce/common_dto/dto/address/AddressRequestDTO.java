package com.ecommerce.common_dto.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Data Transfer Object (DTO) for capturing address details in client requests.
 * This class is used to encapsulate address-related information such as door number,
 * street, city, state, pin code, and country.
 * Validations are applied to ensure data integrity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {

    /**
     * Door number of the address.
     * Must be a positive number.
     */
    @Positive(message = "Door number must be positive")
    private long doorNum;

    /**
     * Street name of the address.
     * Cannot be null or blank.
     */
    @NotBlank(message = "Street is required")
    private String street;

    /**
     * City name.
     * Cannot be null or blank.
     */
    @NotBlank(message = "City is required")
    private String city;

    /**
     * State name.
     * Cannot be null or blank.
     */
    @NotBlank(message = "State is required")
    private String state;

    /**
     * Pin code of the address.
     * Must be exactly 6 digits.
     */
    @NotBlank(message = "Pin code is required")
    @Pattern(regexp = "\\d{6}", message = "Pin code must be 6 digits")
    private String pinCode;

    /**
     * Country name.
     * Cannot be null or blank.
     */
    @NotBlank(message = "Country is required")
    private String country;
}

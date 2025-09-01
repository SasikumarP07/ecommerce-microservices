package com.ecommerce.common_dto.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for capturing address details during sign-up.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {

    private Long doorNum;

    @NotBlank(message = "Street must not be blank")
    private String street;

    @NotBlank(message = "City must not be blank")
    private String city;

    @NotBlank(message = "State must not be blank")
    private String state;

    @NotBlank(message = "Pin code must not be blank")
    private String pinCode;

    @NotBlank(message = "Country must not be blank")
    private String country;
}

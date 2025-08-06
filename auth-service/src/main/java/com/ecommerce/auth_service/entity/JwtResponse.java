package com.ecommerce.auth_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response object for returning the JWT token after successful authentication.
 * This class is typically used in login and signup responses.
 */
@Data
@AllArgsConstructor
public class JwtResponse {

    /**
     * The JWT token issued to the user after successful login or signup.
     * This token is used to authorize future requests.
     */
    private String token;
}

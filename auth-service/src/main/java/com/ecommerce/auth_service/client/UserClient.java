package com.ecommerce.auth_service.client;

import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Feign client interface for communicating with the User Service.
 * This interface allows the Auth Service to create user profiles
 * by calling the User Service endpoint after successful authentication.
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * Sends a request to create a new user in the User Service.
     * @param userProfileRequest the user profile data to be saved
     * @param token              the JWT token used for authorization
     * @return ResponseEntity containing the result of the user creation process
     */
    @PostMapping("/users/save")
    ResponseEntity<String> createUser(
            @RequestBody UserProfileRequest userProfileRequest,
            @RequestHeader("Authorization") String token
    );
}

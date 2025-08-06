package com.ecommerce.payment_service.client;

import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the User Service.
 * <p>
 * This interface enables the Payment Service to fetch user details
 * (such as email or name) based on the user ID.
 * Useful for sending payment-related notifications or logs.
 * </p>
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * Retrieves user details from the User Service using the given user ID.
     *
     * @param userId the unique identifier of the user
     * @return the user details as a {@link UserResponseDTO}
     */
    @GetMapping("/users/{userId}")
    UserResponseDTO getUserById(@PathVariable("userId") Long userId);
}

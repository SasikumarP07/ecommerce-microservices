package com.ecommerce.order_service.client;

import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the User Service.
 * <p>
 * This interface is used to fetch user details by making REST calls
 * to the User Service registered in Eureka.
 */
@FeignClient(name = "user-service")
public interface UserClient {

    /**
     * Retrieves user details from the User Service based on the user ID.
     *
     * @param userId The unique identifier of the user to fetch.
     * @return {@link UserResponseDTO} containing user details.
     */
    @GetMapping("/users/{userId}")
    UserResponseDTO getUserById(@PathVariable("userId") Long userId);
}

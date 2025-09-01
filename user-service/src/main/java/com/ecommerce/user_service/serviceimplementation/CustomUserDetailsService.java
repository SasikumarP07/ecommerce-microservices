package com.ecommerce.user_service.serviceimplementation;

import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Custom implementation of Spring Security's {@link UserDetailsService}
 * to provide user details for authentication based on email.
 * <p>
 * This service is responsible for loading a user from the database using their email address,
 * and converting the {@link User} entity to Spring Security's {@link UserDetails}.
 * </p>
 *
 * @author Sasi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Repository for accessing user-related data.
     */
    private final UserRepository userRepository;

    /**
     * Loads the user details based on the provided email.
     *
     * @param email the email of the user to be loaded
     * @return the {@link UserDetails} object containing user's authentication information
     * @throws UsernameNotFoundException if the user is not found in the database
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        log.info("Attempting to load user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for email: {}", email);
                    return new UsernameNotFoundException("User not found");
                });

        log.info("User found with ID: {}, Role: {}", user.getId(), user.getRole());

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getId()),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}

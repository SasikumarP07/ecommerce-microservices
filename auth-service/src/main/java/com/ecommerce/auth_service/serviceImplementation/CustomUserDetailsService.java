package com.ecommerce.auth_service.serviceImplementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * CustomUserDetailsService is a Spring Security component that implements the UserDetailsService interface.
 * This class is used by Spring Security to load user-specific data during authentication.
 *
 * <p>
 * Note: This implementation currently returns a dummy user with static credentials.
 * In a real-world scenario, this should fetch user details from a persistent database or external system.
 * </p>
 */
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * Loads the user details by username. This method is used by Spring Security during authentication.
     *
     * @param username the username (typically email) used to authenticate the user
     * @return UserDetails containing user credentials and authorities
     * @throws UsernameNotFoundException if the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("🔍 Attempting to load user by username: {}", username);

        // This is a dummy user for testing or fallback scenarios.
        // In production, this should fetch user details from a database or external service.
        UserDetails user = User.withUsername(username)
                .password("password") // ⚠️ Hardcoded password — replace with actual encoded password in real use
                .authorities("ROLE_USER")
                .build();

        log.info("✅ Dummy user loaded with username: {}", username);
        return user;
    }
}

package com.ecommerce.auth_service.repository;

import com.ecommerce.auth_service.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing AuthUser entities.
 * Provides methods for common database operations on authentication users.
 */
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    /**
     * Checks if a user with the given email already exists.
     *
     * @param email The email to check.
     * @return true if a user with the given email exists, false otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Retrieves an AuthUser entity by email.
     *
     * @param email The email of the user.
     * @return An Optional containing the AuthUser if found, or empty if not.
     */
    Optional<AuthUser> findByEmail(String email);
}

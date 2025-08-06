package com.ecommerce.user_service.repository;

import com.ecommerce.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link User} entity.
 *
 * <p>This interface extends {@link JpaRepository}, providing methods like:
 * <ul>
 *     <li>save()</li>
 *     <li>findById()</li>
 *     <li>findAll()</li>
 *     <li>deleteById()</li>
 *     <li>and more...</li>
 * </ul>
 *
 * <p>Includes custom query method to find a user by email.
 *
 * @author YourName
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   /**
    * Finds a user by email.
    *
    * @param email the email to search for
    * @return an {@link Optional} containing the {@link User} if found, otherwise empty
    */
   Optional<User> findByEmail(String email);
}

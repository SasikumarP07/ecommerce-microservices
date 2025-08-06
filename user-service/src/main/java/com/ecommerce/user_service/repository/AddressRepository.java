package com.ecommerce.user_service.repository;

import com.ecommerce.user_service.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for performing CRUD operations on the {@link Address} entity.
 *
 * <p>This interface extends {@link JpaRepository}, providing built-in methods such as:
 * <ul>
 *     <li>save()</li>
 *     <li>findById()</li>
 *     <li>findAll()</li>
 *     <li>deleteById()</li>
 *     <li>and more...</li>
 * </ul>
 *
 * <p>Custom query methods (if needed) can also be defined here.
 *
 * @author YourName
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

}

package com.ecommerce.user_service.service;

import com.ecommerce.user_service.entity.Address;

import java.util.Optional;

/**
 * Service interface for managing {@link Address} entities.
 *
 * <p>This interface defines the core operations related to addresses, such as:
 * <ul>
 *     <li>Saving a new or existing address</li>
 *     <li>Fetching an address by its ID</li>
 *     <li>Deleting an address by its ID</li>
 * </ul>
 *
 * @author YourName
 */
public interface AddressService {

    /**
     * Saves the given address.
     * This method can be used for both creating and updating an address.
     *
     * @param address the address to save
     * @return the saved {@link Address} entity
     */
    Address save(Address address);

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address
     * @return an {@link Optional} containing the address if found, or empty if not found
     */
    Optional<Address> getAddressId(Long addressId);

    /**
     * Deletes the address with the specified ID.
     *
     * @param id the ID of the address to delete
     */
    void delete(Long id);
}

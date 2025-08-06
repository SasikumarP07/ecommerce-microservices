package com.ecommerce.user_service.serviceimplementation;

import com.ecommerce.user_service.entity.Address;
import com.ecommerce.user_service.repository.AddressRepository;
import com.ecommerce.user_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the {@link AddressService} interface.
 * <p>
 * This service provides methods to manage {@link Address} entities, such as saving,
 * retrieving by ID, and deleting addresses.
 * </p>
 *
 * ✅ Uses constructor injection via Lombok's {@link RequiredArgsConstructor}.
 * ✅ Logs all major operations using {@link Slf4j}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImplementation implements AddressService {

    /** Repository for performing CRUD operations on Address entities. */
    private final AddressRepository addressRepository;

    /**
     * Saves a new or existing address entity to the database.
     *
     * @param address the {@link Address} entity to be saved
     * @return the saved {@link Address} with generated ID
     */
    @Override
    public Address save(Address address) {
        log.info("💾 Saving address: {}", address);
        Address savedAddress = addressRepository.save(address);
        log.info("✅ Address saved with ID: {}", savedAddress.getId());
        return savedAddress;
    }

    /**
     * Retrieves an address by its ID.
     *
     * @param addressId the ID of the address to fetch
     * @return an {@link Optional} containing the found address, or empty if not found
     */
    @Override
    public Optional<Address> getAddressId(Long addressId) {
        log.info("🔍 Fetching address with ID: {}", addressId);
        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isPresent()) {
            log.info("✅ Address found: {}", address.get());
        } else {
            log.warn("⚠️ Address not found for ID: {}", addressId);
        }
        return address;
    }

    /**
     * Deletes an address by its ID.
     *
     * @param id the ID of the address to be deleted
     */
    @Override
    public void delete(Long id) {
        log.info("🗑️ Deleting address with ID: {}", id);
        addressRepository.deleteById(id);
        log.info("✅ Address deleted with ID: {}", id);
    }
}

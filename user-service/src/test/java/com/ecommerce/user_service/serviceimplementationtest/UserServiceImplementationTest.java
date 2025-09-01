package com.ecommerce.user_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.address.AddressResponseDTO;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.user_service.entity.*;
import com.ecommerce.user_service.exception.*;
import com.ecommerce.user_service.repository.UserRepository;
import com.ecommerce.user_service.service.AddressService;
import com.ecommerce.user_service.serviceimplementation.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link UserServiceImplementation}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private UserServiceImplementation userService;

    private User user;
    private Address address;
    private UserRequestDTO userRequestDTO;
    private AddressRequestDTO addressRequestDTO;

    /**
     * Initializes test data before each test.
     */
    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John");
        user.setPhone("1234567890");
        user.setEmail("john@example.com");
        user.setPassword("pass123");

        address = new Address();
        address.setId(100L);
        address.setDoorNum(12L);
        address.setStreet("Old Street");
        address.setCity("Old City");
        address.setState("State");
        address.setPinCode("123456");
        address.setCountry("Country");
        address.setUser(user);

        List<Address> addressList = new ArrayList<>();
        addressList.add(address);
        user.setAddresses(addressList);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Updated");
        userRequestDTO.setPhone("0987654321");
        userRequestDTO.setEmail("updated@example.com");
        userRequestDTO.setPassword("newpass");

        addressRequestDTO = new AddressRequestDTO();
        addressRequestDTO.setDoorNum(99L);
        addressRequestDTO.setStreet("New Street");
        addressRequestDTO.setCity("New City");
        addressRequestDTO.setState("New State");
        addressRequestDTO.setPinCode("654321");
        addressRequestDTO.setCountry("New Country");
    }

    /**
     * Tests fetching a user by ID.
     */
    @Test
    void testGetUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponseDTO dto = userService.getUserById(1L);

        assertEquals("John", dto.getName());
        assertEquals("john@example.com", dto.getEmail());
    }

    /**
     * Tests updating an existing user.
     */
    @Test
    void testUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO response = userService.updateUser(1L, userRequestDTO);

        assertNotNull(response);
        assertEquals("Updated", response.getName());
    }

    /**
     * Tests fetching all addresses for a user.
     */
    @Test
    void testGetAddresses() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<AddressResponseDTO> addresses = userService.getAddresses(1L);

        assertNotNull(addresses);
        assertEquals(1, addresses.size());
        assertEquals("Old Street", addresses.get(0).getStreet());
    }

    /**
     * Tests adding an address to a user.
     */
    @Test
    void testAddAddress() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressService.save(any(Address.class))).thenReturn(address);

        AddressResponseDTO response = userService.addAddress(1L, addressRequestDTO);

        assertNotNull(response);
        assertEquals("Old Street", response.getStreet());
    }

    /**
     * Tests updating an existing address for a user.
     */
    @Test
    void testUpdateAddress() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressService.getAddressId(100L)).thenReturn(Optional.of(address));
        when(addressService.save(any(Address.class))).thenReturn(address);

        AddressResponseDTO response = userService.updateAddress(1L, 100L, addressRequestDTO);

        assertNotNull(response);
        assertEquals("New Street", response.getStreet());
    }

    /**
     * Tests deleting a user address.
     */
    @Test
    void testDeleteAddress() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(addressService.getAddressId(100L)).thenReturn(Optional.of(address));

        AddressResponseDTO response = userService.deleteAddress(1L, 100L);

        assertNotNull(response);
        assertEquals("Old Street", response.getStreet());

        verify(addressService, times(1)).delete(100L);
    }

    /**
     * Tests behavior when a user is not found.
     */
    @Test
    void testGetUser_NotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(2L));
    }
}

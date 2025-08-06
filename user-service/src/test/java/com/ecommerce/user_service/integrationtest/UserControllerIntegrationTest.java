package com.ecommerce.user_service.integrationtest;

import com.ecommerce.common_dto.dto.address.AddressRequestDTO;
import com.ecommerce.common_dto.dto.user.UserProfileRequest;
import com.ecommerce.common_dto.dto.user.UserRequestDTO;
import com.ecommerce.common_util.util.JwtUtil;
import com.ecommerce.user_service.entity.User;
import com.ecommerce.user_service.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for UserController REST APIs.
 * This class tests full-stack behavior including persistence, service, and controller layers.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private Long userId;
    private String token;

    /**
     * Set up a test user and generate a JWT token before each test case.
     */
    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhone("1234567890");
        user.setPassword("password");
        user.setRole("ROLE_USER");

        User saved = userRepository.save(user);
        userId = saved.getId();
        token = jwtUtil.generateToken(userId, "ROLE_USER");
    }

    /**
     * Test creating a new user via POST /users/save endpoint.
     */
    @Test
    void testCreateUser() throws Exception {
        UserProfileRequest userProfileRequest = new UserProfileRequest();
        userProfileRequest.setId(1L);
        userProfileRequest.setName("Alice");
        userProfileRequest.setEmail("alice@example.com");
        userProfileRequest.setPhone("9876543210");
        userProfileRequest.setPassword("password");
        userProfileRequest.setRole("ROLE_USER");

        mockMvc.perform(post("/users/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(userProfileRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Alice")))
                .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    /**
     * Test fetching a user by ID via GET /users/{id} endpoint.
     */
    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/users/{id}", userId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }

    /**
     * Test updating user details via PUT /users/{id} endpoint.
     */
    @Test
    void testUpdateUser() throws Exception {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setName("Updated Name");
        dto.setEmail("updated@example.com");
        dto.setPhone("9876543210");
        dto.setPassword("newPassword");

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Name")))
                .andExpect(jsonPath("$.email", is("updated@example.com")));
    }

    /**
     * Test adding an address for a user via POST /users/{id}/addresses endpoint.
     */
    @Test
    void testAddAddress() throws Exception {
        AddressRequestDTO addressRequest = new AddressRequestDTO();
        addressRequest.setDoorNum(101L);
        addressRequest.setStreet("New Street");
        addressRequest.setCity("New City");
        addressRequest.setState("State");
        addressRequest.setPinCode("123456");
        addressRequest.setCountry("India");

        mockMvc.perform(post("/users/{id}/addresses", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", is("New Street")));
    }

    /**
     * Test retrieving addresses for a user via GET /users/{id}/addresses endpoint.
     */
    @Test
    void testGetAddresses() throws Exception {
        testAddAddress(); // Ensure an address exists

        mockMvc.perform(get("/users/{id}/addresses", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(jsonPath("$[0].street", is("New Street")));
    }

    /**
     * Test updating an existing address via PUT /users/{id}/addresses/{addressId} endpoint.
     */
    @Test
    void testUpdateAddress() throws Exception {
        // First, add an address
        AddressRequestDTO addressRequest = new AddressRequestDTO();
        addressRequest.setDoorNum(101L);
        addressRequest.setStreet("New Street");
        addressRequest.setCity("New City");
        addressRequest.setState("State");
        addressRequest.setPinCode("123456");
        addressRequest.setCountry("India");

        String response = mockMvc.perform(post("/users/{id}/addresses", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long addressId = objectMapper.readTree(response).get("id").asLong();

        // Update the address
        addressRequest.setStreet("Updated Street");

        mockMvc.perform(put("/users/{id}/addresses/{addressId}", userId, addressId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", is("Updated Street")));
    }

    /**
     * Test deleting an address via DELETE /users/{id}/addresses/{addressId} endpoint.
     */
    @Test
    void testDeleteAddress() throws Exception {
        // First, add an address
        AddressRequestDTO addressRequest = new AddressRequestDTO();
        addressRequest.setDoorNum(101L);
        addressRequest.setStreet("Temp Street");
        addressRequest.setCity("City");
        addressRequest.setState("State");
        addressRequest.setPinCode("000000");
        addressRequest.setCountry("India");

        String response = mockMvc.perform(post("/users/{id}/addresses", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long addressId = objectMapper.readTree(response).get("id").asLong();

        // Now, delete it
        mockMvc.perform(delete("/users/{id}/addresses/{addressId}", userId, addressId))
                .andExpect(status().isNoContent());
    }
}

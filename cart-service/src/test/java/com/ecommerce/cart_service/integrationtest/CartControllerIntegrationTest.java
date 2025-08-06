package com.ecommerce.cart_service.integrationtest;

import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartItemUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for CartController.
 * <p>
 * These tests verify the full workflow of Cart APIs by starting the application context
 * and testing the controller endpoints using MockMvc.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    private final String BASE_URL = "/api/carts";

    /**
     * Test to verify adding a product to the cart.
     * It sends a POST request with productId and quantity and expects a successful response.
     */
    @Test
    void testAddToCart() throws Exception {
        CartItemRequestDTO request = new CartItemRequestDTO();
        request.setProductId(100L);
        request.setQuantity(2);

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    /**
     * Test to verify updating an item’s quantity in the cart.
     * Sends a PUT request with updated quantity and expects success status.
     */
    @Test
    void testUpdateCartItem() throws Exception {
        CartItemUpdateDTO updateRequest = new CartItemUpdateDTO();
        updateRequest.setProductId(100L);
        updateRequest.setQuantity(5);

        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    /**
     * Test to retrieve all cart items by a specific user ID.
     * Expects a successful GET response.
     */
    @Test
    void testGetCartItemsByUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + "/user/1"))
                .andExpect(status().isOk());
    }

    /**
     * Test to remove a specific item from the user's cart.
     * Sends a DELETE request with userId and productId as parameters.
     */
    @Test
    void testRemoveCartItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/remove")
                        .param("userId", "1")
                        .param("productId", "100"))
                .andExpect(status().isOk());
    }

    /**
     * Test to clear all items from a user's cart.
     * Sends a DELETE request with the userId as a parameter.
     */
    @Test
    void testClearCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + "/clear")
                        .param("userId", "1"))
                .andExpect(status().isOk());
    }
}

package com.ecommerce.order_service.integrationtest;

import com.ecommerce.common_dto.dto.order.OrderItemRequestDTO;
import com.ecommerce.common_dto.dto.order.OrderRequestDTO;
import com.ecommerce.common_dto.dto.order.OrderResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@code OrderController}.
 * <p>
 * These tests use {@link MockMvc} to simulate HTTP requests and verify the full behavior of the order API endpoints
 * in a running Spring Boot application context.
 */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderRequestDTO orderRequest;

    /**
     * Set up a basic {@link OrderRequestDTO} object to be used across multiple test cases.
     */
    @BeforeEach
    void setUp() {
        OrderItemRequestDTO item = OrderItemRequestDTO.builder()
                .productId(1L)
                .quantity(2)
                .build();

        orderRequest = OrderRequestDTO.builder()
                .userId(1L)
                .items(Collections.singletonList(item))
                .build();
    }

    /**
     * Test case for placing a new order.
     * <p>
     * Verifies that the API returns HTTP 201 Created and that the response contains the expected user ID.
     */
    @Test
    void placeOrder_ReturnsCreatedOrder() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    /**
     * Test case for retrieving an order by its ID.
     * <p>
     * First, it places a new order and then fetches it using its ID. Verifies that the fetched order matches.
     */
    @Test
    void getOrderById_ReturnsOrder() throws Exception {
        String response = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OrderResponseDTO createdOrder = objectMapper.readValue(response, OrderResponseDTO.class);

        mockMvc.perform(get("/orders/" + createdOrder.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdOrder.getId()));
    }

    /**
     * Test case for retrieving all orders placed by a specific user.
     * <p>
     * Assumes that the user with ID 1 has placed at least one order.
     */
    @Test
    void getOrdersByUserId_ReturnsOrders() throws Exception {
        mockMvc.perform(get("/orders/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    /**
     * Test case for retrieving all orders in the system.
     * <p>
     * Verifies that at least one order exists and has a valid ID.
     */
    @Test
    void getAllOrders_ReturnsOrderList() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    /**
     * Test case for canceling an order.
     * <p>
     * Places an order, deletes it, and verifies that the operation returns HTTP 204 No Content.
     */
    @Test
    void cancelOrder_ReturnsNoContent() throws Exception {
        String response = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        OrderResponseDTO createdOrder = objectMapper.readValue(response, OrderResponseDTO.class);

        mockMvc.perform(delete("/orders/" + createdOrder.getId()))
                .andExpect(status().isNoContent());
    }
}

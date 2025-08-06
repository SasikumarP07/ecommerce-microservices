package com.ecommerce.inventory_service.integrationtest;

import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the InventoryController in the Inventory Service.
 * This class uses Spring Boot's testing framework to test the full flow of HTTP requests
 * against the REST API exposed by the InventoryController. It performs integration testing
 * by loading the application context, using a real in-memory database (H2), and verifying
 * controller behavior through HTTP calls using MockMvc.
 * Dependencies:
 * - MockMvc: Simulates HTTP requests/responses without running a server.
 * - ObjectMapper: Serializes and deserializes JSON objects.
 * - InventoryRepository: Used to prepare and clean the database state.
 * Tested Endpoints:
 * - POST /api/inventory
 * - GET /api/inventory/{productId}
 * - PUT /api/inventory/{productId}?quantity=...
 * - POST /api/inventory/{productId}/reduce?quantity=...
 * Setup:
 * Before each test, the database is cleared and seeded with one sample Inventory entity.
 */
@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Inventory savedInventory;

    /**
     * Sets up the test environment by clearing the database and inserting one sample inventory record.
     */
    @BeforeEach
    void setUp() {
        inventoryRepository.deleteAll(); // clean DB

        Inventory inventory = Inventory.builder()
                .productId(101L)
                .quantity(50)
                .build();
        savedInventory = inventoryRepository.save(inventory);
    }

    /**
     * Test case for creating a new inventory entry.
     * Verifies that the POST request returns 200 OK with the correct productId and quantity.
     */
    @Test
    void testCreateInventory() throws Exception {
        InventoryRequestDTO request = InventoryRequestDTO.builder()
                .productId(202L)
                .quantity(100)
                .build();

        mockMvc.perform(post("/api/inventory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(202L))
                .andExpect(jsonPath("$.quantity").value(100));
    }

    /**
     * Test case for fetching inventory by productId.
     * Verifies that the GET request returns 200 OK with the expected product details.
     */
    @Test
    void testGetInventory() throws Exception {
        mockMvc.perform(get("/api/inventory/{productId}", savedInventory.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(savedInventory.getProductId()))
                .andExpect(jsonPath("$.quantity").value(savedInventory.getQuantity()));
    }

    /**
     * Test case for updating the stock quantity of a product.
     * Verifies that the PUT request returns 200 OK and reflects the updated quantity.
     */
    @Test
    void testUpdateStock() throws Exception {
        mockMvc.perform(put("/api/inventory/{productId}?quantity=80", savedInventory.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(80));
    }

    /**
     * Test case for reducing stock quantity after an order.
     * Verifies that the stock is reduced and updated correctly in the database.
     */
    @Test
    void testReduceStock() throws Exception {
        mockMvc.perform(post("/api/inventory/{productId}/reduce?quantity=10", savedInventory.getProductId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock reduced successfully"));

        mockMvc.perform(get("/api/inventory/{productId}", savedInventory.getProductId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(savedInventory.getQuantity() - 10));
    }
}

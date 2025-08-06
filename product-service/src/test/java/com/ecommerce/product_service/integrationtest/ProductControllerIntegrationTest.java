package com.ecommerce.product_service.integrationtest;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the ProductController.
 * This test suite validates the core functionality of product-related endpoints,
 * including CRUD operations, pagination, filtering, and user-specific suggestions.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequestDTO sampleRequest;

    /**
     * Initializes a sample product request DTO before each test case.
     */
    @BeforeEach
    public void setup() {
        sampleRequest = new ProductRequestDTO();
        sampleRequest.setName("Test Product");
        sampleRequest.setDescription("Test Description");
        sampleRequest.setPrice(BigDecimal.valueOf(999.99));
        sampleRequest.setBrand("Test Brand");
        sampleRequest.setQuantityAvailable(100);
        sampleRequest.setCategoryId(1L);
        sampleRequest.setImageUrls(List.of("http://image.com/test1.jpg"));
    }

    /**
     * Verifies that a product can be successfully created.
     */
    @Test
    public void testCreateProduct() throws Exception {
        ResultActions result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleRequest)));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    /**
     * Verifies fetching a product by ID returns correct product data.
     */
    @Test
    public void testGetProductById() throws Exception {
        ProductResponseDTO created = objectMapper.readValue(mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andReturn().getResponse().getContentAsString(), ProductResponseDTO.class);

        mockMvc.perform(get("/products/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    /**
     * Verifies that paginated products are returned successfully.
     */
    @Test
    public void testGetAllProductsPaginated() throws Exception {
        mockMvc.perform(get("/products?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    /**
     * Verifies a product can be deleted by ID.
     */
    @Test
    public void testDeleteProduct() throws Exception {
        ProductResponseDTO created = objectMapper.readValue(mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andReturn().getResponse().getContentAsString(), ProductResponseDTO.class);

        mockMvc.perform(delete("/products/" + created.getId()))
                .andExpect(status().isNoContent());
    }

    /**
     * Verifies that product filtering and sorting by parameters works correctly.
     */
    @Test
    public void testFilterAndSortProducts() throws Exception {
        mockMvc.perform(get("/products/filter")
                        .param("name", "Test")
                        .param("sortBy", "price")
                        .param("direction", "asc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that the API returns the top 10 latest products.
     */
    @Test
    public void testGetTop10LatestProducts() throws Exception {
        mockMvc.perform(get("/products/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * Verifies that the API returns suggested products for a user.
     */
    @Test
    public void testGetSuggestedProducts() throws Exception {
        mockMvc.perform(get("/products/suggested/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    /**
     * Verifies that updating a product modifies its attributes as expected.
     */
    @Test
    public void testUpdateProduct() throws Exception {
        ProductResponseDTO created = objectMapper.readValue(mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andReturn().getResponse().getContentAsString(), ProductResponseDTO.class);

        sampleRequest.setName("Updated Name");

        mockMvc.perform(put("/products/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }
}

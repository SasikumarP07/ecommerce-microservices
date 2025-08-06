package com.ecommerce.product_service.integrationtest;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the CategoryController.
 * This test class verifies CRUD operations, access control, and API behavior for category-related endpoints.
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Long createdCategoryId;

    /**
     * Initializes the test environment by clearing existing categories
     * and inserting a default "Electronics" category.
     */
    @BeforeEach
    void setUp() throws Exception {
        categoryRepository.deleteAll(); // Clean up before each test

        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Electronics");
        requestDTO.setDescription("Electronic gadgets");

        String response = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO))
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        createdCategoryId = objectMapper.readTree(response).get("id").asLong();
    }

    /**
     * Tests that an admin user can successfully create a new category.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateCategory() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Books");
        requestDTO.setDescription("All kinds of books");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Books"));
    }

    /**
     * Tests that a user with the USER role can retrieve a category by its ID.
     */
    @Test
    @WithMockUser(roles = "USER")
    void testGetCategoryById() throws Exception {
        mockMvc.perform(get("/categories/{id}", createdCategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    /**
     * Tests that all categories can be fetched by a user.
     */
    @Test
    @WithMockUser(roles = "USER")
    void testGetAllCategories() throws Exception {
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Electronics")));
    }

    /**
     * Tests retrieving a category by its name using a query parameter.
     */
    @Test
    @WithMockUser(roles = "USER")
    void testGetCategoryByName() throws Exception {
        mockMvc.perform(get("/categories/by-name")
                        .param("name", "Electronics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Electronic gadgets"));
    }

    /**
     * Tests that an admin user can update an existing category.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCategory() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Updated Electronics");
        requestDTO.setDescription("Updated Description");

        mockMvc.perform(put("/categories/{id}", createdCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    /**
     * Tests that an admin user can delete an existing category.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCategory() throws Exception {
        mockMvc.perform(delete("/categories/{id}", createdCategoryId))
                .andExpect(status().isNoContent());
    }

    /**
     * Tests that a user with the USER role is forbidden from creating a category.
     */
    @Test
    @WithMockUser(roles = "USER")
    void testUnauthorizedAccessToCreateCategory() throws Exception {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO();
        requestDTO.setName("Test");
        requestDTO.setDescription("Test Desc");

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }
}

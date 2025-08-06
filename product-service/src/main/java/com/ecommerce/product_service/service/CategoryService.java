package com.ecommerce.product_service.service;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;

import java.util.List;

/**
 * Service interface for managing product categories.
 * <p>
 * Defines the contract for creating, retrieving, updating, and deleting product categories.
 * Implementations should handle business logic and interactions with the repository layer.
 * </p>
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param requestDTO the DTO containing category data
     * @return the created category as a response DTO
     */
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    /**
     * Retrieves all available categories.
     *
     * @return a list of category response DTOs
     */
    List<CategoryResponseDTO> getAllCategories();

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return the category response DTO
     */
    CategoryResponseDTO getCategoryById(Long id);

    /**
     * Retrieves a category by its name.
     *
     * @param name the name of the category
     * @return the category response DTO
     */
    CategoryResponseDTO getCategoryByName(String name);

    /**
     * Updates an existing category.
     *
     * @param id         the ID of the category to update
     * @param requestDTO the updated category data
     * @return the updated category response DTO
     */
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO);

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    void deleteCategory(Long id);
}

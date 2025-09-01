package com.ecommerce.product_service.serviceimplementation;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.exception.DuplicateResourceException;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.mapper.CategoryMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation of the {@link CategoryService} interface for handling
 * category-related business logic.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImplementation implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * Creates a new category if it doesn't already exist.
     *
     * @param requestDTO the category creation request
     * @return the created category as a DTO
     * @throws DuplicateResourceException if the category name already exists
     */
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        log.info("📦 Creating new category: {}", requestDTO.getName());

        if (categoryRepository.existsByName(requestDTO.getName())) {
            log.warn("Category already exists with name: {}", requestDTO.getName());
            throw new DuplicateResourceException("Category with name already exists");
        }

        Category saved = categoryRepository.save(CategoryMapper.toEntity(requestDTO));
        log.info("Category created with ID: {}", saved.getId());
        return CategoryMapper.toDto(saved);
    }

    /**
     * Retrieves all categories from the database.
     *
     * @return a list of category DTOs
     */
    @Override
    public List<CategoryResponseDTO> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryRepository.findAll();
        log.debug("🔎 Found {} categories", categories.size());

        List<CompletableFuture<CategoryResponseDTO>> futureList = categories.stream()
                .map(CategoryMapper::toDTOAsync)
                .toList();

        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[0])).join();

        List<CategoryResponseDTO> result = futureList.stream()
                .map(CompletableFuture::join)
                .toList();

        log.info("All categories mapped to DTOs successfully");
        return result;
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category
     * @return the corresponding category DTO
     * @throws ResourceNotFoundException if no category is found with the given ID
     */
    @Override
    public CategoryResponseDTO getCategoryById(Long id) {
        log.info("Fetching category by ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });

        log.info("Category found: {}", category.getName());
        return CategoryMapper.toDto(category);
    }

    /**
     * Updates the name and description of an existing category.
     *
     * @param id         the ID of the category to update
     * @param requestDTO the new data for the category
     * @return the updated category as a DTO
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO requestDTO) {
        log.info("Updating category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot update. Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });

        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());

        Category updated = categoryRepository.save(category);
        log.info("Category updated: {}", updated.getName());
        return CategoryMapper.toDto(updated);
    }

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    public void deleteCategory(Long id) {
        log.info("Deleting category with ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cannot delete. Category not found with ID: {}", id);
                    return new ResourceNotFoundException("Category not found with ID: " + id);
                });

        categoryRepository.delete(category);
        log.info("Category deleted with ID: {}", id);
    }

    /**
     * Retrieves a category by its name.
     *
     * @param name the name of the category
     * @return the corresponding category DTO
     * @throws ResourceNotFoundException if the category is not found
     */
    @Override
    public CategoryResponseDTO getCategoryByName(String name) {
        log.info("Fetching category by name: {}", name);

        Category category = categoryRepository.findByName(name)
                .orElseThrow(() -> {
                    log.error("Category not found with name: {}", name);
                    return new ResourceNotFoundException("Category not found with name: " + name);
                });

        log.info("Category found: {}", category.getName());
        return CategoryMapper.toDto(category);
    }
}

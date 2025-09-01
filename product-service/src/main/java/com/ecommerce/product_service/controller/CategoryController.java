package com.ecommerce.product_service.controller;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;
import com.ecommerce.product_service.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * CategoryController handles HTTP requests related to category operations in the Product Service.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Received request to create category: {}", requestDTO.getName());
        CategoryResponseDTO created = categoryService.createCategory(requestDTO);
        log.info("Category created successfully with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        log.info("Received request to fetch category by ID: {}", id);
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        log.info("Fetched category: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        log.info("Received request to fetch all categories");
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        log.info("Total categories fetched: {}", categories.size());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(@RequestParam String name) {
        log.info("Received request to fetch category by name: {}", name);
        CategoryResponseDTO category = categoryService.getCategoryByName(name);
        log.info("Fetched category: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Received request to update category with ID: {}", id);
        CategoryResponseDTO updated = categoryService.updateCategory(id, requestDTO);
        log.info("Category updated successfully: {}", updated.getName());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Received request to delete category with ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Category deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}

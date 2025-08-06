package com.ecommerce.product_service.controller;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;
import com.ecommerce.product_service.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * 📦 CategoryController handles HTTP requests related to category operations in the Product Service.
 */
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Controller", description = "APIs for managing product categories.")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Create Category", description = "Create a new product category. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Received request to create category: {}", requestDTO.getName());
        CategoryResponseDTO created = categoryService.createCategory(requestDTO);
        log.info("Category created successfully with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get Category by ID", description = "Retrieve a category by its ID. Accessible to ADMIN and USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(
            @Parameter(description = "ID of the category") @PathVariable Long id) {
        log.info("Received request to fetch category by ID: {}", id);
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        log.info("Fetched category: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Get All Categories", description = "Retrieve all product categories. Accessible to ADMIN and USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories retrieved successfully")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        log.info("Received request to fetch all categories");
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        log.info("Total categories fetched: {}", categories.size());
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Get Category by Name", description = "Retrieve a category by its name. Accessible to ADMIN and USER.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/by-name")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> getCategoryByName(
            @Parameter(description = "Name of the category") @RequestParam String name) {
        log.info("Received request to fetch category by name: {}", name);
        CategoryResponseDTO category = categoryService.getCategoryByName(name);
        log.info("Fetched category: {}", category.getName());
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Update Category", description = "Update a category by its ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @Parameter(description = "ID of the category to update") @PathVariable Long id,
            @RequestBody CategoryRequestDTO requestDTO) {
        log.info("Received request to update category with ID: {}", id);
        CategoryResponseDTO updated = categoryService.updateCategory(id, requestDTO);
        log.info("Category updated successfully: {}", updated.getName());
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete Category", description = "Delete a category by its ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCategory(
            @Parameter(description = "ID of the category to delete") @PathVariable Long id) {
        log.info("Received request to delete category with ID: {}", id);
        categoryService.deleteCategory(id);
        log.info("Category deleted successfully with ID: {}", id);
        return ResponseEntity.noContent().build();
    }
}

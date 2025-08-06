package com.ecommerce.product_service.controller;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Product Controller", description = "Operations related to product management")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Create a new product", description = "Creates a product with the given details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        log.info("📦 Creating new product: {}", requestDTO.getName());
        ProductResponseDTO created = productService.createProduct(requestDTO);
        log.info("✅ Product created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get product by ID", description = "Fetches a product based on the provided ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(
            @Parameter(description = "ID of the product to fetch") @PathVariable Long id) {
        log.info("🔍 Fetching product by ID: {}", id);
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Get paginated list of products", description = "Retrieves products with pagination support")
    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("📄 Fetching paginated products - page: {}, size: {}", page, size);
        Page<ProductResponseDTO> paged = productService.getAllProductsPaginated(page, size);
        return ResponseEntity.ok(paged);
    }

    @Operation(summary = "Delete a product by ID", description = "Deletes a product given its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @Parameter(description = "ID of the product to delete") @PathVariable Long id) {
        log.info("🗑️ Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("✅ Product deleted successfully");
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Filter & sort products", description = "Filter and sort products by name, category, price, etc.")
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductResponseDTO>> filterAndSortProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("📊 Filtering products | name: {}, categoryId: {}, price: {}-{}, sortBy: {}, direction: {}, page: {}, size: {}",
                name, categoryId, minPrice, maxPrice, sortBy, direction, page, size);
        Page<ProductResponseDTO> filtered = productService.filterAndSortProductsPaginated(
                name, categoryId, minPrice, maxPrice, sortBy, direction, page, size
        );
        return ResponseEntity.ok(filtered);
    }

    @Operation(summary = "Get top 10 latest products", description = "Retrieves the latest 10 added products")
    @GetMapping("/latest")
    public ResponseEntity<List<ProductResponseDTO>> getTop10LatestProducts() {
        log.info("🆕 Fetching top 10 latest products");
        List<ProductResponseDTO> latest = productService.getTop10LatestProducts();
        return ResponseEntity.ok(latest);
    }

    @Operation(summary = "Get suggested products for user", description = "Fetches product suggestions for a given user ID")
    @GetMapping("/suggested/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> getSuggestedProducts(
            @Parameter(description = "User ID for suggestions") @PathVariable Long userId) {
        log.info("🤖 Fetching suggested products for userId: {}", userId);
        List<ProductResponseDTO> suggested = productService.getSuggestedProductsForUser(userId);
        return ResponseEntity.ok(suggested);
    }

    @Operation(summary = "Update product", description = "Updates a product given its ID and new details")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @Parameter(description = "ID of the product to update") @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        log.info("✏️ Updating product with ID: {}", id);
        ProductResponseDTO updated = productService.updateProduct(id, requestDTO);
        log.info("✅ Product updated successfully: {}", updated.getId());
        return ResponseEntity.ok(updated);
    }
}

package com.ecommerce.product_service.controller;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.product_service.service.ProductService;
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
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        log.info("Creating new product: {}", requestDTO.getName());
        ProductResponseDTO created = productService.createProduct(requestDTO);
        log.info("Product created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        log.info("Fetching product by ID: {}", id);
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAllProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("Fetching paginated products - page: {}, size: {}", page, size);
        Page<ProductResponseDTO> paged = productService.getAllProductsPaginated(page, size);
        return ResponseEntity.ok(paged);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with ID: {}", id);
        productService.deleteProduct(id);
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build();
    }

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
        log.info("Filtering products | name: {}, categoryId: {}, price: {}-{}, sortBy: {}, direction: {}, page: {}, size: {}",
                name, categoryId, minPrice, maxPrice, sortBy, direction, page, size);
        Page<ProductResponseDTO> filtered = productService.filterAndSortProductsPaginated(
                name, categoryId, minPrice, maxPrice, sortBy, direction, page, size
        );
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<ProductResponseDTO>> getTop10LatestProducts() {
        log.info("Fetching top 10 latest products");
        List<ProductResponseDTO> latest = productService.getTop10LatestProducts();
        return ResponseEntity.ok(latest);
    }

    @GetMapping("/suggested/{userId}")
    public ResponseEntity<List<ProductResponseDTO>> getSuggestedProducts(@PathVariable Long userId) {
        log.info("Fetching suggested products for userId: {}", userId);
        List<ProductResponseDTO> suggested = productService.getSuggestedProductsForUser(userId);
        return ResponseEntity.ok(suggested);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        log.info("Updating product with ID: {}", id);
        ProductResponseDTO updated = productService.updateProduct(id, requestDTO);
        log.info("Product updated successfully: {}", updated.getId());
        return ResponseEntity.ok(updated);
    }
}

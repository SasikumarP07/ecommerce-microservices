package com.ecommerce.product_service.service;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for handling product-related operations.
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param requestDTO DTO containing product data.
     * @return The created product details.
     */
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product.
     * @return The product details.
     */
    ProductResponseDTO getProductById(Long productId);

    /**
     * Retrieves a paginated list of all products.
     *
     * @param page The page number (0-indexed).
     * @param size The number of products per page.
     * @return A paginated list of products.
     */
    Page<ProductResponseDTO> getAllProductsPaginated(int page, int size);

    /**
     * Deletes a product by its ID.
     *
     * @param productId The ID of the product to delete.
     */
    void deleteProduct(Long productId);

    /**
     * Filters and sorts products with pagination support.
     *
     * @param name       Product name filter (nullable).
     * @param categoryId Category ID filter (nullable).
     * @param minPrice   Minimum price filter (nullable).
     * @param maxPrice   Maximum price filter (nullable).
     * @param sortBy     Field to sort by (e.g., price, name).
     * @param direction  Sort direction ("asc" or "desc").
     * @param page       Page number (0-indexed).
     * @param size       Page size.
     * @return A paginated list of filtered and sorted products.
     */
    Page<ProductResponseDTO> filterAndSortProductsPaginated(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String direction,
            int page,
            int size
    );

    /**
     * Retrieves the top 10 latest added products.
     *
     * @return List of latest products.
     */
    List<ProductResponseDTO> getTop10LatestProducts();

    /**
     * Suggests products for a specific user based on their preferences or history.
     *
     * @param userId The ID of the user.
     * @return List of suggested products.
     */
    List<ProductResponseDTO> getSuggestedProductsForUser(Long userId);

    /**
     * Updates an existing product.
     *
     * @param productId  The ID of the product to update.
     * @param requestDTO DTO containing updated product data.
     * @return The updated product details.
     */
    ProductResponseDTO updateProduct(Long productId, ProductRequestDTO requestDTO);
}

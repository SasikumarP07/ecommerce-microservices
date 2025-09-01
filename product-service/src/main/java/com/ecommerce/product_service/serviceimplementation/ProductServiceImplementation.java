package com.ecommerce.product_service.serviceimplementation;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.product_service.client.InventoryClient;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.entity.ProductImage;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.mapper.ProductMapper;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.service.ProductService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link ProductService} interface.
 * Handles business logic for product operations like creation, retrieval,
 * update, deletion, filtering, sorting, and interaction with the inventory service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImplementation implements ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final InventoryClient inventoryClient;

    /**
     * Creates a new product and initializes its inventory to 0.
     *
     * @param dto the product creation request DTO
     * @return the created product as a response DTO
     */
    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        log.info("Creating new product with name: {}", dto.getName());

        Product saved = productRepository.save(ProductMapper.dtoToEntity(dto));
        log.info("Product saved with ID: {}", saved.getId());

        InventoryRequestDTO inventoryRequest = InventoryRequestDTO.builder()
                .productId(saved.getId())
                .quantity(0)
                .build();

        try {
            inventoryClient.createInventory(inventoryRequest);
            log.info("Inventory initialized for product ID: {}", saved.getId());
        } catch (Exception e) {
            log.error("Failed to create inventory for product ID: {}", saved.getId(), e);
        }

        return ProductMapper.entityToDto(saved);
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product
     * @return the product as a response DTO
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    log.warn("Product not found with ID: {}", productId);
                    return new ResourceNotFoundException("Product not found with id: " + productId);
                });
        return ProductMapper.entityToDto(product);
    }

    /**
     * Retrieves all products in a paginated format.
     *
     * @param page the page number
     * @param size the page size
     * @return a page of product response DTOs
     */
    @Override
    public Page<ProductResponseDTO> getAllProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ProductMapper::entityToDto);
    }

    /**
     * Deletes a product by ID.
     *
     * @param productId the ID of the product
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            log.warn("Tried to delete non-existing product with ID: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
        log.info("Product deleted with ID: {}", productId);
    }

    /**
     * Filters and sorts products based on given criteria and returns paginated results.
     *
     * @param name       filter by name (partial match)
     * @param categoryId filter by category ID
     * @param minPrice   minimum price
     * @param maxPrice   maximum price
     * @param sortBy     field to sort by
     * @param direction  sort direction ("asc" or "desc")
     * @param page       page number
     * @param size       page size
     * @return a page of filtered and sorted product response DTOs
     */
    @Override
    public Page<ProductResponseDTO> filterAndSortProductsPaginated(
            String name,
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sortBy,
            String direction,
            int page,
            int size
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> filtered = productRepository.findAll((root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (categoryId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), categoryId));
            }
            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return predicate;
        }, pageable);

        return filtered.map(ProductMapper::entityToDto);
    }

    /**
     * Retrieves the 10 latest products by creation date.
     *
     * @return a list of the top 10 latest product response DTOs
     */
    @Override
    public List<ProductResponseDTO> getTop10LatestProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        return productRepository.findAll(pageable)
                .stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves suggested products for a user.
     * (Currently returns the 5 latest products as a placeholder)
     *
     * @param userId the ID of the user
     * @return a list of suggested product response DTOs
     */
    @Override
    public List<ProductResponseDTO> getSuggestedProductsForUser(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        return productRepository.findAll(pageable)
                .stream()
                .map(ProductMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing product.
     *
     * @param productId the ID of the product to update
     * @param dto       the updated product details
     * @return the updated product as a response DTO
     * @throws ResourceNotFoundException if the product is not found
     */
    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductRequestDTO dto) {
        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setPrice(dto.getPrice());
        existing.setBrand(dto.getBrand());
        existing.setQuantityAvailable(dto.getQuantityAvailable());

        if (dto.getCategoryId() != null) {
            Category category = new Category();
            category.setId(dto.getCategoryId());
            existing.setCategory(category);
        }

        List<ProductImage> images = dto.getImageUrls().stream()
                .map(url -> {
                    ProductImage image = new ProductImage();
                    image.setImageUrl(url);
                    image.setProduct(existing);
                    return image;
                }).collect(Collectors.toList());

        existing.getImageUrls().clear();
        existing.getImageUrls().addAll(images);

        Product updated = productRepository.save(existing);
        log.info("Product updated with ID: {}", updated.getId());
        return ProductMapper.entityToDto(updated);
    }
}

package com.ecommerce.product_service.mapper;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.entity.ProductImage;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for mapping between {@link Product} entities and their corresponding DTOs.
 */
@Slf4j
public class ProductMapper {

    /**
     * Converts a {@link ProductRequestDTO} to a {@link Product} entity.
     *
     * @param dto the product request DTO containing product creation or update details
     * @return a Product entity populated from the DTO
     */
    public static Product dtoToEntity(ProductRequestDTO dto) {
        log.info("Mapping ProductRequestDTO to Product entity: {}", dto);

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setBrand(dto.getBrand());
        product.setQuantityAvailable(dto.getQuantityAvailable());

        // Set category (only ID is provided in DTO)
        Category category = new Category();
        category.setId(dto.getCategoryId());
        product.setCategory(category);
        log.debug("Mapped category with ID: {}", dto.getCategoryId());

        // Map image URLs to ProductImage entities
        List<ProductImage> images = dto.getImageUrls().stream()
                .map(url -> {
                    ProductImage image = new ProductImage();
                    image.setImageUrl(url);
                    image.setProduct(product);
                    return image;
                })
                .collect(Collectors.toList());

        product.setImageUrls(images);
        log.debug("Mapped {} image URLs to ProductImage list", images.size());

        return product;
    }

    /**
     * Converts a {@link Product} entity to a {@link ProductResponseDTO}.
     *
     * @param product the entity to convert
     * @return a ProductResponseDTO populated from the entity
     */
    public static ProductResponseDTO entityToDto(Product product) {
        log.info("Mapping Product entity to ProductResponseDTO: Product ID {}", product.getId());

        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setBrand(product.getBrand());
        dto.setQuantityAvailable(product.getQuantityAvailable());

        // Set category details
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
            log.debug("Set category info: ID={}, Name={}", product.getCategory().getId(), product.getCategory().getName());
        }

        // Convert ProductImage entities to list of URLs
        List<String> imageUrls = product.getImageUrls().stream()
                .map(ProductImage::getImageUrl)
                .collect(Collectors.toList());

        dto.setImageUrls(imageUrls);
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        log.debug("Mapped {} images and timestamps", imageUrls.size());

        return dto;
    }
}

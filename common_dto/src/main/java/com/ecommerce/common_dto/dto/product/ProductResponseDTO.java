package com.ecommerce.common_dto.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for sending product details as a response.
 * Contains all necessary fields including product metadata, category info, and image URLs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    /** Unique identifier for the product */
    private Long id;

    /** Name of the product */
    private String name;

    /** Detailed description of the product */
    private String description;

    /** Price of the product */
    private BigDecimal price;

    /** Brand name of the product */
    private String brand;

    /** Quantity of the product available in inventory */
    private int quantityAvailable;

    /** ID of the category the product belongs to */
    private Long categoryId;

    /** Name of the category the product belongs to */
    private String categoryName;

    /** List of image URLs associated with the product */
    private List<String> imageUrls;

    /** Timestamp when the product was created */
    private LocalDateTime createdAt;

    /** Timestamp when the product was last updated */
    private LocalDateTime updatedAt;
}

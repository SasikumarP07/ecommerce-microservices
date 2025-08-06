package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an image associated with a product in the e-commerce application.
 * Each product can have multiple images.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImage {

    /**
     * Unique identifier for the product image.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * URL of the image associated with the product.
     */
    private String imageUrl;

    /**
     * The product to which this image belongs.
     */
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}

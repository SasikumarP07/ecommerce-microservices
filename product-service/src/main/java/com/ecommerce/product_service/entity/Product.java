package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a product in the e-commerce application.
 * Contains information such as name, description, price, brand, availability, category, and images.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * Unique identifier for the product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the product.
     */
    private String name;

    /**
     * Description of the product.
     */
    private String description;

    /**
     * Price of the product.
     */
    private BigDecimal price;

    /**
     * Brand of the product.
     */
    private String brand;

    /**
     * Quantity available in stock.
     */
    private int quantityAvailable;

    /**
     * The category to which the product belongs.
     */
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * List of image URLs associated with the product.
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> imageUrls = new ArrayList<>();

    /**
     * The timestamp when the product was created.
     */
    private LocalDateTime createdAt;

    /**
     * The timestamp when the product was last updated.
     */
    private LocalDateTime updatedAt;

    /**
     * Sets the createdAt and updatedAt fields to the current time before the product is persisted.
     */
    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    /**
     * Updates the updatedAt field to the current time before the product is updated.
     */
    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}

package com.ecommerce.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * Represents a product category in the e-commerce application.
 * Each category can have multiple products associated with it.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /**
     * Unique identifier for the category.
     * This ID is auto-generated using IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the category (e.g., Electronics, Fashion, etc.).
     */
    private String name;

    /**
     * Description providing additional details about the category.
     */
    private String description;

    /**
     * List of products belonging to this category.
     * This is a one-to-many relationship where one category can have multiple products.
     * Cascade type is ALL to propagate operations to associated products.
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    /**
     * Custom constructor excluding the product list.
     *
     * @param id          ID of the category.
     * @param name        Name of the category.
     * @param description Description of the category.
     */
    public Category(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}

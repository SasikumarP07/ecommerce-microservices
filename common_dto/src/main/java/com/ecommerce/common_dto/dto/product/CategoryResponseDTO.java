package com.ecommerce.common_dto.dto.product;

import lombok.*;

/**
 * DTO used to return category details in API responses.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    /**
     * Unique identifier for the category.
     */
    private Long id;

    /**
     * Name of the category.
     */
    private String name;

    /**
     * Description of the category.
     */
    private String description;
}

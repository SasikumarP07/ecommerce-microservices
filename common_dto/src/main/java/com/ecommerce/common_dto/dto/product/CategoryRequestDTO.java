package com.ecommerce.common_dto.dto.product;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for creating or updating a product category.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

    /**
     * Name of the category.
     */
    @NotBlank(message = "Category name must not be blank")
    private String name;

    /**
     * Description of the category.
     */
    @NotBlank(message = "Category description must not be blank")
    private String description;
}

package com.ecommerce.common_dto.dto.product;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * DTO used to accept product data during create/update operations.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    /**
     * Name of the product.
     */
    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    /**
     * Description of the product.
     */
    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    /**
     * Price of the product.
     */
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    private BigDecimal price;

    /**
     * Brand of the product.
     */
    @NotBlank(message = "Brand is required")
    private String brand;

    /**
     * Quantity available in stock.
     */
    @Min(value = 0, message = "Quantity available cannot be negative")
    private int quantityAvailable;

    /**
     * ID of the category the product belongs to.
     */
    @NotNull(message = "Category ID is required")
    private Long categoryId;

    /**
     * List of image URLs for the product.
     */
    @NotEmpty(message = "At least one image URL is required")
    private List<
            @NotBlank(message = "Image URL cannot be blank")
                    String> imageUrls;
}

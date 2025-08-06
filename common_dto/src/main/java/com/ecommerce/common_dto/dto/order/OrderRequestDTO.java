package com.ecommerce.common_dto.dto.order;

import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * Request DTO for placing an order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    /**
     * ID of the user placing the order.
     */
    @NotNull(message = "User ID must not be null")
    private Long userId;

    /**
     * List of items in the order. Must contain at least one valid item.
     */
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<@Valid OrderItemRequestDTO> items;
}

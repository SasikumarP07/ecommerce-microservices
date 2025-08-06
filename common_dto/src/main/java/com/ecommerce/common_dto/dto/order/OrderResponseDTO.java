package com.ecommerce.common_dto.dto.order;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response DTO representing a placed order.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    /**
     * Unique identifier of the order.
     */
    private Long id;

    /**
     * ID of the user who placed the order.
     */
    private Long userId;

    /**
     * Timestamp of when the order was placed.
     */
    private LocalDateTime orderDate;

    /**
     * List of items included in the order.
     */
    private List<OrderItemResponseDTO> items;
}

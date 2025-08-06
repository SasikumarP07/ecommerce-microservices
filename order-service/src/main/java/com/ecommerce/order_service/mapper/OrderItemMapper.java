package com.ecommerce.order_service.mapper;

import com.ecommerce.common_dto.dto.order.*;
import com.ecommerce.order_service.entity.OrderItem;
import lombok.extern.slf4j.Slf4j;

/**
 * Mapper utility class for converting between {@link OrderItemRequestDTO},
 * {@link OrderItemResponseDTO}, and {@link OrderItem} entity.
 * This class provides static methods for:
 * - Converting a request DTO to an entity (`toEntity`)
 * - Converting an entity to a response DTO (`toDTO`)
 * Logging is enabled using SLF4J to trace the mapping process.
 */
@Slf4j
public class OrderItemMapper {

    /**
     * Converts an {@link OrderItemRequestDTO} to an {@link OrderItem} entity.
     *
     * @param dto the OrderItemRequestDTO containing input data for order item
     * @return a new {@link OrderItem} entity populated with product ID and quantity
     */
    public static OrderItem toEntity(OrderItemRequestDTO dto) {
        log.info("Mapping OrderItemRequestDTO to OrderItem entity: {}", dto);
        return OrderItem.builder()
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .build();
    }

    /**
     * Converts an {@link OrderItem} entity to an {@link OrderItemResponseDTO}.
     *
     * @param item the OrderItem entity containing order item details
     * @return a new {@link OrderItemResponseDTO} populated with product and pricing details
     */
    public static OrderItemResponseDTO toDTO(OrderItem item) {
        log.info("Mapping OrderItem entity to OrderItemResponseDTO: {}", item);
        return OrderItemResponseDTO.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .productName(item.getProductName())
                .productPrice(item.getProductPrice())
                .quantity(item.getQuantity())
                .totalPrice(item.getTotalPrice())
                .build();
    }
}

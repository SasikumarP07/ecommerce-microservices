package com.ecommerce.order_service.mapper;

import com.ecommerce.common_dto.dto.order.*;
import com.ecommerce.order_service.entity.Order;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Utility class responsible for converting between {@link OrderRequestDTO}, {@link Order}, and {@link OrderResponseDTO}.
 * <p>
 * This class contains static methods to:
 * <ul>
 *   <li>Convert {@link OrderRequestDTO} to {@link Order} entity.</li>
 *   <li>Convert {@link Order} entity to {@link OrderResponseDTO}.</li>
 * </ul>
 * Logging is used to trace the mapping process for debugging and auditing.
 */
@Slf4j
public class OrderMapper {

    /**
     * Converts an {@link OrderRequestDTO} to an {@link Order} entity.
     *
     * @param dto The DTO containing order request data from the client.
     * @return A new {@link Order} entity populated with the user ID and current timestamp as order date.
     */
    public static Order toEntity(OrderRequestDTO dto) {
        log.info("Mapping OrderRequestDTO to Order entity: {}", dto);
        return Order.builder()
                .userId(dto.getUserId())
                .orderDate(LocalDateTime.now()) // Set the order date to the current timestamp
                .build();
    }

    /**
     * Converts an {@link Order} entity to an {@link OrderResponseDTO}.
     *
     * @param order The {@link Order} entity to convert.
     * @return A {@link OrderResponseDTO} populated with order details and mapped order items.
     */
    public static OrderResponseDTO toDTO(Order order) {
        log.info("Mapping Order entity to OrderResponseDTO: {}", order);
        return OrderResponseDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .orderDate(order.getOrderDate())
                .items(order.getItems() != null
                        ? order.getItems().stream()
                        .map(item -> {
                            log.debug("Mapping OrderItem: {}", item);
                            return OrderItemMapper.toDTO(item);
                        })
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}

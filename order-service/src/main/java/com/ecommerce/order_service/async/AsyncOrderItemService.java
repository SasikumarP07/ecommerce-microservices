package com.ecommerce.order_service.async;

import com.ecommerce.common_dto.dto.order.*;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.mapper.OrderItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

/**
 * Service class responsible for building {@link OrderItem} instances asynchronously.
 * This is used to enhance performance when creating multiple order items in parallel.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncOrderItemService {

    private final ProductClient productClient;

    /**
     * Asynchronously builds an {@link OrderItem} entity by enriching it with product details
     * fetched from the {@link ProductClient}.
     *
     * @param order   The parent {@link Order} to which this item belongs.
     * @param itemDTO The {@link OrderItemRequestDTO} containing basic order item info.
     * @return A {@link CompletableFuture} wrapping the fully constructed {@link OrderItem} entity,
     *         or null in case of failure.
     */
    @Async
    public CompletableFuture<OrderItem> buildOrderItemAsync(Order order, OrderItemRequestDTO itemDTO) {
        try {
            ProductResponseDTO product = productClient.getProductById(itemDTO.getProductId());

            OrderItem item = OrderItemMapper.toEntity(itemDTO);
            item.setOrder(order);
            item.setProductName(product.getName());
            item.setProductPrice(product.getPrice());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            return CompletableFuture.completedFuture(item);
        } catch (Exception e) {
            log.error("Error building OrderItem for productId: {}", itemDTO.getProductId(), e);
            return CompletableFuture.completedFuture(null);
        }
    }
}

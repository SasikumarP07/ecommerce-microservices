package com.ecommerce.cart_service.mapper;

import com.ecommerce.cart_service.client.ProductServiceClient;
import com.ecommerce.cart_service.entity.Cart;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.serviceimplementation.ProductAsyncService;
import com.ecommerce.common_dto.dto.cart.CartItemResponseDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Mapper class to convert {@link Cart} entities into {@link CartResponseDTO} objects.
 * Supports both synchronous and asynchronous mapping of product details from ProductService.
 * Useful for transforming domain entities into API response structures.
 */
@Slf4j
public class CartMapper {

    /**
     * Converts a {@link Cart} entity to {@link CartResponseDTO} asynchronously by fetching product details in parallel.
     *
     * @param cart          The cart entity to convert.
     * @param asyncService  Service to asynchronously fetch product details.
     * @return A fully populated {@link CartResponseDTO} with item-level product data, or {@code null} if input is invalid.
     */
    public static CartResponseDTO toResponseDTO(Cart cart, ProductAsyncService asyncService) {
        if (cart == null || cart.getItems() == null) {
            log.warn("Cannot map null cart or items in async method.");
            return null;
        }

        List<CompletableFuture<CartItemResponseDTO>> futures = cart.getItems().stream()
                .map(item -> asyncService.fetchProduct(item.getProductId())
                        .thenApply(product -> mapToCartItemResponse(item, product)))
                .collect(Collectors.toList());

        List<CartItemResponseDTO> itemDTOs = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        CartResponseDTO response = new CartResponseDTO(cart.getId(), cart.getUserId(), itemDTOs,cart.getTotalPrice());
        log.debug("Async CartResponseDTO created: {}", response);
        return response;
    }

    /**
     * Converts a {@link Cart} entity to {@link CartResponseDTO} synchronously by calling the ProductService.
     *
     * @param cart           The cart entity to convert.
     * @param productClient  Feign client to synchronously fetch product details.
     * @return A fully populated {@link CartResponseDTO} with item-level product data, or {@code null} if input is invalid.
     */
    public static CartResponseDTO toResponseDTO(Cart cart, ProductServiceClient productClient) {
        if (cart == null || cart.getItems() == null) {
            log.warn("Cannot map null cart or items in sync method.");
            return null;
        }

        List<CartItemResponseDTO> itemDTOs = cart.getItems().stream()
                .map(item -> {
                    ProductResponseDTO product = productClient.getProductById(item.getProductId());
                    return mapToCartItemResponse(item, product);
                })
                .collect(Collectors.toList());

        CartResponseDTO response = new CartResponseDTO(cart.getId(), cart.getUserId(), itemDTOs,cart.getTotalPrice());
        log.debug("Sync CartResponseDTO created: {}", response);
        return response;
    }

    /**
     * Maps a {@link CartItem} and its corresponding {@link ProductResponseDTO} to a {@link CartItemResponseDTO}.
     *
     * @param item     The cart item containing product ID and quantity.
     * @param product  The product details fetched from ProductService.
     * @return A {@link CartItemResponseDTO} combining item and product data, or {@code null} if any argument is {@code null}.
     */
    private static CartItemResponseDTO mapToCartItemResponse(CartItem item, ProductResponseDTO product) {
        if (item == null || product == null) {
            log.error("Cannot map CartItemResponseDTO: item or product is null.");
            return null;
        }

        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        return new CartItemResponseDTO(
                item.getId(),
                item.getProductId(),
                product.getName(),
                product.getPrice(),
                item.getQuantity(),
                totalPrice
        );
    }
}

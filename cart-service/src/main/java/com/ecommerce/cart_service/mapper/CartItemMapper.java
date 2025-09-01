package com.ecommerce.cart_service.mapper;

import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.common_dto.dto.cart.CartItemResponseDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * Utility class for mapping CartItem entities to CartItemResponseDTOs.
 * This helps separate entity logic from DTO logic for clean architecture.
 */
@Slf4j
public class CartItemMapper {

    /**
     * Converts a CartItem entity and corresponding ProductResponseDTO into a CartItemResponseDTO.
     *
     * @param item    The CartItem entity from the database.
     * @param product The ProductResponseDTO fetched from ProductService.
     * @return A fully populated CartItemResponseDTO or null if item/product is null.
     */
    public static CartItemResponseDTO toDto(CartItem item, ProductResponseDTO product) {
        // Validate input
        if (item == null || product == null) {
            log.warn("Cannot map CartItem to DTO: item or product is null. item={}, product={}", item, product);
            return null;
        }

        // Log mapping start
        log.debug("Mapping CartItem to CartItemResponseDTO: itemId={}, productName={}, quantity={}",
                item.getId(), product.getName(), item.getQuantity());

        // Create and populate DTO
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductName(product.getName());
        dto.setProductPrice(product.getPrice());
        dto.setQuantity(item.getQuantity());

        // Calculate total price: productPrice * quantity
        dto.setTotalPrice(product.getPrice().multiply(new BigDecimal(item.getQuantity())));

        // Log mapping result
        log.debug("Mapped DTO: {}", dto);

        return dto;
    }
}

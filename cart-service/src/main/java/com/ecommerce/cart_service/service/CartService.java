package com.ecommerce.cart_service.service;

import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;

/**
 * 🛒 Service interface for managing user cart operations.
 * Provides contract methods for:
 * - retrieving a cart,
 * - adding items,
 * - updating item quantities,
 * - removing items,
 * - and clearing the cart.
 */
public interface CartService {

    /**
     * 🔍 Get the cart for a specific user by user ID.
     *
     * @param userId the ID of the user
     * @return the current cart details as {@link CartResponseDTO}
     */
    CartResponseDTO getCartByUserId(Long userId);

    /**
     * ➕ Add a new item to the user's cart.
     *
     * @param userId         the ID of the user
     * @param itemRequestDTO details of the item to be added
     * @return updated cart details
     */
    CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO itemRequestDTO);

    /**
     * 🔄 Update the quantity of an existing cart item.
     *
     * @param userId   the ID of the user
     * @param itemId   the ID of the cart item
     * @param quantity new quantity to be set
     * @return updated cart details
     */
    CartResponseDTO updateItemQuantity(Long userId, Long itemId, int quantity);

    /**
     * ❌ Remove a specific item from the user's cart.
     *
     * @param userId the ID of the user
     * @param itemId the ID of the cart item to be removed
     * @return updated cart details
     */
    CartResponseDTO removeItemFromCart(Long userId, Long itemId);

    /**
     * 🧹 Clear all items from the user's cart.
     *
     * @param userId the ID of the user
     */
    void clearCart(Long userId);
}

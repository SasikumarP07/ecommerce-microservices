package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.service.CartService;
import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartItemUpdateDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUserId(@PathVariable Long userId) {
        log.info("Fetching cart for userId: {}", userId);
        CartResponseDTO response = cartService.getCartByUserId(userId);
        log.info("Cart fetched successfully for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequestDTO requestDTO
    ) {
        log.info("Adding item to cart for userId: {}, productId: {}, quantity: {}",
                userId, requestDTO.getProductId(), requestDTO.getQuantity());
        CartResponseDTO response = cartService.addItemToCart(userId, requestDTO);
        log.info("Item added successfully to cart for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemUpdateDTO updateDTO
    ) {
        log.info("Updating quantity in cart for userId: {}, productId: {}, new quantity: {}",
                userId, updateDTO.getProductId(), updateDTO.getQuantity());
        CartResponseDTO response = cartService.updateItemQuantity(
                userId,
                updateDTO.getProductId(),
                updateDTO.getQuantity()
        );
        log.info("Item quantity updated successfully for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        log.info("Removing item from cart for userId: {}, productId: {}", userId, productId);
        CartResponseDTO response = cartService.removeItemFromCart(userId, productId);
        log.info("Item removed successfully from cart for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        log.info("Clearing cart for userId: {}", userId);
        cartService.clearCart(userId);
        log.info("Cart cleared successfully for userId: {}", userId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }
}

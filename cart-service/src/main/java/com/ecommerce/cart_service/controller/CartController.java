package com.ecommerce.cart_service.controller;

import com.ecommerce.cart_service.service.CartService;
import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartItemUpdateDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cart", description = "Operations related to the shopping cart")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get user's cart", description = "Fetch the cart associated with a specific user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully",
            content = @Content(schema = @Schema(implementation = CartResponseDTO.class)))
    @GetMapping("/{userId}")
    public ResponseEntity<CartResponseDTO> getCartByUserId(@PathVariable Long userId) {
        log.info("Fetching cart for userId: {}", userId);
        CartResponseDTO response = cartService.getCartByUserId(userId);
        log.info("Cart fetched successfully for userId: {}", userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Add item to cart", description = "Add a new item to the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item added to cart",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Item details to be added to cart",
            required = true,
            content = @Content(schema = @Schema(implementation = CartItemRequestDTO.class))
    )
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

    @Operation(summary = "Update cart item quantity", description = "Update the quantity of an item in the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart updated successfully",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class)))
    })
    @PutMapping("/{userId}/items")
    public ResponseEntity<CartResponseDTO> updateItemQuantity(
            @PathVariable Long userId,
            @Parameter(
                    description = "Updated item quantity",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CartItemUpdateDTO.class))
            )
            @Valid @RequestBody CartItemUpdateDTO updateDTO
    ){
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

    @Operation(summary = "Remove item from cart", description = "Remove an item from the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item removed successfully",
                    content = @Content(schema = @Schema(implementation = CartResponseDTO.class)))
    })
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

    @Operation(summary = "Clear user's cart", description = "Remove all items from the user's cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart cleared successfully",
                    content = @Content(schema = @Schema(implementation = String.class)))
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        log.info("Clearing cart for userId: {}", userId);
        cartService.clearCart(userId);
        log.info("Cart cleared successfully for userId: {}", userId);
        return ResponseEntity.ok("Cart cleared successfully.");
    }
}

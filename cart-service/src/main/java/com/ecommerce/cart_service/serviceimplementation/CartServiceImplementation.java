package com.ecommerce.cart_service.serviceimplementation;

import com.ecommerce.cart_service.client.ProductServiceClient;
import com.ecommerce.cart_service.entity.Cart;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.exception.InvalidInputException;
import com.ecommerce.cart_service.exception.ResourceNotFoundException;
import com.ecommerce.cart_service.mapper.CartMapper;
import com.ecommerce.cart_service.repository.CartRepository;
import com.ecommerce.cart_service.service.CartService;
import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 🛠️ Service implementation for handling cart-related operations.
 * This class provides business logic for:
 * - Adding, updating, and removing items in a user's cart
 * - Fetching the current state of the cart
 * - Clearing the cart
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImplementation implements CartService {

    private final CartRepository cartRepository;
    private final ProductServiceClient productClient;
    private final ProductAsyncService productAsyncService;

    /**
     * ➕ Adds a new item to the user's cart or updates the quantity if the item already exists.
     *
     * @param userId     the ID of the user
     * @param requestDTO the item request containing productId and quantity
     * @return updated cart details
     */
    @Override
    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO) {
        log.info("Adding item to cart for userId: {}", userId);

        if (userId == null || requestDTO.getProductId() == null || requestDTO.getQuantity() <= 0) {
            log.error("Invalid input for adding item to cart. userId: {}, productId: {}, quantity: {}", userId, requestDTO.getProductId(), requestDTO.getQuantity());
            throw new InvalidInputException("Invalid userId, productId, or quantity");
        }

        ProductResponseDTO product = productClient.getProductById(requestDTO.getProductId());
        log.debug("Fetched product info: {}", product);

        Cart cart = cartRepository.findByUserId(userId).orElseGet(() -> {
            log.info("No cart found for userId: {}. Creating new cart.", userId);
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return newCart;
        });

        Optional<CartItem> existingItemOpt = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(requestDTO.getProductId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem item = existingItemOpt.get();
            item.setQuantity(item.getQuantity() + requestDTO.getQuantity());
            log.info("Updated quantity of existing product in cart. productId: {}, newQuantity: {}", item.getProductId(), item.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(requestDTO.getProductId());
            newItem.setQuantity(requestDTO.getQuantity());
            cart.getItems().add(newItem);
            log.info("Added new product to cart. productId: {}", newItem.getProductId());
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Cart updated successfully for userId: {}", userId);
        return CartMapper.toResponseDTO(savedCart, productClient);
    }

    /**
     * 🔍 Fetches the cart details for a specific user using async product fetching.
     *
     * @param userId the ID of the user
     * @return cart response with detailed product info
     */
    @Override
    @Transactional(readOnly = true)
    public CartResponseDTO getCartByUserId(Long userId) {
        log.info("Fetching cart for userId: {}", userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new ResourceNotFoundException("Cart not found for userId: " + userId);
                });

        log.info("Cart retrieved successfully for userId: {}", userId);
        return CartMapper.toResponseDTO(cart, productAsyncService);
    }

    /**
     * ❌ Removes a specific product from the user's cart.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product to remove
     * @return updated cart response
     */
    @Override
    @Transactional
    public CartResponseDTO removeItemFromCart(Long userId, Long productId) {
        log.info("Removing item from cart. userId: {}, productId: {}", userId, productId);

        if (userId == null || productId == null) {
            log.error("Invalid input for removing item from cart.");
            throw new InvalidInputException("userId and productId must not be null");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new ResourceNotFoundException("Cart not found for userId: " + userId);
                });

        boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));
        if (!removed) {
            log.error("Product not found in cart: {}", productId);
            throw new ResourceNotFoundException("Product not found in cart: " + productId);
        }

        Cart updatedCart = cartRepository.save(cart);
        log.info("Item removed successfully from cart. productId: {}", productId);
        return CartMapper.toResponseDTO(updatedCart, productClient);
    }

    /**
     * 🧹 Clears all items from the user's cart.
     *
     * @param userId the ID of the user
     */
    @Override
    @Transactional
    public void clearCart(Long userId) {
        log.info("Clearing cart for userId: {}", userId);

        if (userId == null) {
            log.error("Invalid input: userId is null");
            throw new InvalidInputException("userId must not be null");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new ResourceNotFoundException("Cart not found for userId: " + userId);
                });

        cart.getItems().clear();
        cartRepository.save(cart);
        log.info("Cart cleared successfully for userId: {}", userId);
    }

    /**
     * 🔄 Updates the quantity of a specific item in the user's cart.
     *
     * @param userId   the ID of the user
     * @param productId the product whose quantity is being updated
     * @param quantity the new quantity to set
     * @return updated cart response
     */
    @Override
    @Transactional
    public CartResponseDTO updateItemQuantity(Long userId, Long productId, int quantity) {
        log.info("Updating item quantity. userId: {}, productId: {}, quantity: {}", userId, productId, quantity);

        if (userId == null || productId == null || quantity <= 0) {
            log.error("Invalid input for updating item quantity.");
            throw new InvalidInputException("Invalid input: userId, productId or quantity");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Cart not found for userId: {}", userId);
                    return new ResourceNotFoundException("Cart not found for userId: " + userId);
                });

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Item not found in cart with productId: {}", productId);
                    return new ResourceNotFoundException("Item not found in cart with productId: " + productId);
                });

        item.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        log.info("Item quantity updated successfully. productId: {}, newQuantity: {}", productId, quantity);
        return CartMapper.toResponseDTO(savedCart, productClient);
    }
}

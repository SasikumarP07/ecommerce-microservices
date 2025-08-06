package com.ecommerce.cart_service.serviceimplementationtest;

import com.ecommerce.cart_service.client.ProductServiceClient;
import com.ecommerce.cart_service.entity.Cart;
import com.ecommerce.cart_service.entity.CartItem;
import com.ecommerce.cart_service.exception.InvalidInputException;
import com.ecommerce.cart_service.repository.CartRepository;
import com.ecommerce.cart_service.serviceimplementation.CartServiceImplementation;
import com.ecommerce.common_dto.dto.cart.CartItemRequestDTO;
import com.ecommerce.common_dto.dto.cart.CartResponseDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CartServiceImplementation}.
 * Uses Mockito to mock dependencies and test business logic in isolation.
 */
class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductServiceClient productClient;

    @InjectMocks
    private CartServiceImplementation cartService;

    /**
     * Initializes mocks before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for adding a new item to the cart when the cart already exists.
     */
    @Test
    void addItemToCart_ShouldAddNewItem() {
        Long userId = 1L;
        Long productId = 10L;
        CartItemRequestDTO request = new CartItemRequestDTO(productId, 2);

        ProductResponseDTO product = new ProductResponseDTO();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(100));

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());

        when(productClient.getProductById(productId)).thenReturn(product);
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        CartResponseDTO response = cartService.addItemToCart(userId, request);
        assertThat(response).isNotNull();
    }

    /**
     * Test for fetching a cart by user ID.
     */
    @Test
    void getCartByUserId_ShouldReturnCart() {
        Long userId = 1L;
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        CartResponseDTO response = cartService.getCartByUserId(userId);
        assertThat(response.getUserId()).isEqualTo(userId);
    }

    /**
     * Test for removing an item from the cart by product ID.
     */
    @Test
    void removeItemFromCart_ShouldRemoveSuccessfully() {
        Long userId = 1L;
        Long productId = 10L;

        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>(java.util.List.of(item)));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        CartResponseDTO response = cartService.removeItemFromCart(userId, productId);
        assertThat(response).isNotNull();
    }

    /**
     * Test for clearing all items in the cart for a given user ID.
     */
    @Test
    void clearCart_ShouldRemoveAllItems() {
        Long userId = 1L;
        CartItem item = new CartItem();
        item.setProductId(10L);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>(java.util.List.of(item)));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

        verify(cartRepository).save(cart);
        assertThat(cart.getItems()).isEmpty();
    }

    /**
     * Test for updating the quantity of an item in the cart.
     */
    @Test
    void updateItemQuantity_ShouldUpdateSuccessfully() {
        Long userId = 1L;
        Long productId = 10L;

        CartItem item = new CartItem();
        item.setProductId(productId);
        item.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>(java.util.List.of(item)));

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenReturn(cart);

        CartResponseDTO response = cartService.updateItemQuantity(userId, productId, 5);
        assertThat(response).isNotNull();
    }

    /**
     * Test for handling null user ID input when adding an item to the cart.
     * Should throw {@link InvalidInputException}.
     */
    @Test
    void addItemToCart_InvalidInput_ShouldThrowException() {
        assertThatThrownBy(() -> cartService.addItemToCart(null, new CartItemRequestDTO()))
                .isInstanceOf(InvalidInputException.class);
    }
}

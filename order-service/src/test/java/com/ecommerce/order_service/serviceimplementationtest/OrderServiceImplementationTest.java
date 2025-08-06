package com.ecommerce.order_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.order.*;
import com.ecommerce.order_service.async.AsyncOrderItemService;
import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.exception.ResourceNotFoundException;
import com.ecommerce.order_service.repository.OrderItemRepository;
import com.ecommerce.order_service.repository.OrderRepository;

import com.ecommerce.order_service.serviceimplementation.OrderServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link com.ecommerce.order_service.serviceimplementation.OrderServiceImplementation}.
 * <p>
 * This test class uses Mockito to mock dependencies and verify the core business logic of the OrderService layer,
 * including order creation, retrieval, and cancellation behaviors.
 */
public class OrderServiceImplementationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private AsyncOrderItemService asyncOrderItemService;

    @InjectMocks
    private OrderServiceImplementation orderService;

    /**
     * Initializes the mock objects before each test case.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for successful order placement.
     * <p>
     * Mocks saving the order and asynchronously building the order item. Verifies the result and persistence calls.
     */
    @Test
    void testPlaceOrder() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setUserId(1L);
        OrderItemRequestDTO item = new OrderItemRequestDTO(101L, 2);
        requestDTO.setItems(List.of(item));

        Order order = Order.builder().userId(1L).build();
        Order savedOrder = Order.builder().id(1L).userId(1L).build();
        OrderItem orderItem = OrderItem.builder()
                .productId(101L)
                .quantity(2)
                .productName("Test Product")
                .productPrice(BigDecimal.valueOf(100))
                .totalPrice(BigDecimal.valueOf(200))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(asyncOrderItemService.buildOrderItemAsync(any(), any()))
                .thenReturn(CompletableFuture.completedFuture(orderItem));

        OrderResponseDTO response = orderService.placeOrder(requestDTO);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).saveAll(anyList());
    }

    /**
     * Test retrieving an order by ID when it exists.
     * <p>
     * Verifies that the returned response DTO matches the expected ID.
     */
    @Test
    void testGetOrderByIdSuccess() {
        Order order = Order.builder().id(1L).userId(1L).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderResponseDTO response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
    }

    /**
     * Test retrieving an order by ID when it does not exist.
     * <p>
     * Expects a {@link ResourceNotFoundException} to be thrown.
     */
    @Test
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    /**
     * Test retrieving all orders placed by a specific user.
     * <p>
     * Verifies that the returned list contains the expected order.
     */
    @Test
    void testGetOrdersByUserId() {
        List<Order> orders = Arrays.asList(Order.builder().id(1L).userId(1L).build());
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        List<OrderResponseDTO> response = orderService.getOrdersByUserId(1L);

        assertEquals(1, response.size());
        assertEquals(1L, response.get(0).getId());
    }

    /**
     * Test retrieving all orders from the repository.
     * <p>
     * Verifies that the service returns the correct number of orders.
     */
    @Test
    void testGetAllOrders() {
        List<Order> orders = Arrays.asList(Order.builder().id(1L).build());
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderResponseDTO> response = orderService.getAllOrders();

        assertEquals(1, response.size());
    }

    /**
     * Test canceling an order that exists.
     * <p>
     * Verifies that the delete method is called on the repository.
     */
    @Test
    void testCancelOrderSuccess() {
        Order order = Order.builder().id(1L).build();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.cancelOrder(1L);

        verify(orderRepository).delete(order);
    }

    /**
     * Test canceling an order that does not exist.
     * <p>
     * Expects a {@link ResourceNotFoundException} to be thrown.
     */
    @Test
    void testCancelOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> orderService.cancelOrder(1L));
    }
}

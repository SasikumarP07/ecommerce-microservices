package com.ecommerce.order_service.serviceimplementation;

import com.ecommerce.common_dto.dto.notification.NotificationRequestDTO;
import com.ecommerce.common_dto.dto.order.*;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.common_dto.dto.user.UserResponseDTO;
import com.ecommerce.order_service.async.AsyncOrderItemService;
import com.ecommerce.order_service.client.NotificationClient;
import com.ecommerce.order_service.client.ProductClient;
import com.ecommerce.order_service.client.UserClient;
import com.ecommerce.order_service.entity.Order;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.exception.ResourceNotFoundException;
import com.ecommerce.order_service.mapper.OrderItemMapper;
import com.ecommerce.order_service.mapper.OrderMapper;
import com.ecommerce.order_service.repository.OrderItemRepository;
import com.ecommerce.order_service.repository.OrderRepository;
import com.ecommerce.order_service.service.OrderService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Implementation of {@link OrderService} interface for handling order-related operations.
 * <p>
 * This class includes placing new orders, retrieving orders by ID or user,
 * fetching all orders, and canceling orders. It also integrates with external services
 * such as Product, Notification, and User services via Feign Clients.
 * <p>
 * Uses asynchronous processing for creating order items.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductClient productClient;
    private final AsyncOrderItemService asyncOrderItemService;
    private final NotificationClient notificationClient;
    private final UserClient userClient;

    /**
     * Places a new order and sends a confirmation notification to the user.
     * <p>
     * Order items are processed asynchronously and saved after the main order is saved.
     *
     * @param requestDTO the order request containing user ID and order items
     * @return {@link OrderResponseDTO} containing saved order details
     */
    @Override
    @Transactional
    public OrderResponseDTO placeOrder(OrderRequestDTO requestDTO) {
        log.info("Placing order for userId: {}", requestDTO.getUserId());

        Order order = OrderMapper.toEntity(requestDTO);
        Order savedOrder = orderRepository.save(order); // Save to generate ID

        List<CompletableFuture<OrderItem>> futures = requestDTO.getItems().stream()
                .map(itemDTO -> asyncOrderItemService.buildOrderItemAsync(savedOrder, itemDTO))
                .collect(Collectors.toList());

        List<OrderItem> orderItems = futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        log.info("Order placed successfully with ID: {}", savedOrder.getId());

        UserResponseDTO user = userClient.getUserById(requestDTO.getUserId());

        NotificationRequestDTO notification = new NotificationRequestDTO();
        notification.setToEmail(user.getEmail());
        notification.setSubject("Order Confirmation");
        notification.setMessage("Your order with ID " + savedOrder.getId() + " has been successfully placed.");
        notificationClient.sendNotification(notification);

        return OrderMapper.toDTO(savedOrder);
    }

    /**
     * Retrieves an order by its ID.
     *
     * @param orderId the ID of the order
     * @return {@link OrderResponseDTO} containing order details
     * @throws ResourceNotFoundException if the order is not found
     */
    @Override
    public OrderResponseDTO getOrderById(Long orderId) {
        log.info("Fetching order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found with ID: " + orderId);
                });
        return OrderMapper.toDTO(order);
    }

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the user's ID
     * @return list of {@link OrderResponseDTO}
     */
    @Override
    public List<OrderResponseDTO> getOrdersByUserId(Long userId) {
        log.info("Fetching orders for userId: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        log.debug("Found {} orders for userId: {}", orders.size(), userId);
        return orders.stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return list of {@link OrderResponseDTO}
     */
    @Override
    public List<OrderResponseDTO> getAllOrders() {
        log.info("Fetching all orders");
        return orderRepository.findAll().stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Cancels an existing order by deleting it from the system.
     *
     * @param orderId the ID of the order to cancel
     * @throws ResourceNotFoundException if the order is not found
     */
    @Override
    public void cancelOrder(Long orderId) {
        log.info("Cancelling order with ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", orderId);
                    return new ResourceNotFoundException("Order not found with ID: " + orderId);
                });

        orderRepository.delete(order);
        log.info("Order cancelled successfully with ID: {}", orderId);
    }

    /**
     * Builds an {@link OrderItem} from request data and product info.
     * <p>
     * This method is used internally by the async service.
     *
     * @param order   the parent order entity
     * @param itemDTO the order item DTO containing productId and quantity
     * @return populated {@link OrderItem}
     */
    private OrderItem buildOrderItem(Order order, OrderItemRequestDTO itemDTO) {
        log.debug("Fetching product details for productId: {}", itemDTO.getProductId());
        ProductResponseDTO product = productClient.getProductById(itemDTO.getProductId());

        OrderItem item = OrderItemMapper.toEntity(itemDTO);
        item.setOrder(order);
        item.setProductName(product.getName());
        item.setProductPrice(product.getPrice());
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

        log.debug("Built order item: productId={}, quantity={}, totalPrice={}",
                itemDTO.getProductId(), itemDTO.getQuantity(), item.getTotalPrice());

        return item;
    }
}

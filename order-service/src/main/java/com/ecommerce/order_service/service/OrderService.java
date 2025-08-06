package com.ecommerce.order_service.service;

import com.ecommerce.common_dto.dto.order.*;
import java.util.List;

/**
 * Service interface for managing orders in the E-commerce application.
 * <p>
 * Provides methods for placing, retrieving, listing, and canceling orders.
 * Implementations of this interface handle business logic related to order processing.
 * <p>
 * Each method returns a DTO to decouple the service layer from the persistence layer.
 *
 * @author YourName
 * @see com.ecommerce.order_service.serviceimplementation.OrderServiceImplementation
 * @see com.ecommerce.common_dto.dto.order.OrderRequestDTO
 * @see com.ecommerce.common_dto.dto.order.OrderResponseDTO
 */
public interface OrderService {

    /**
     * Places a new order based on the provided order request data.
     *
     * @param requestDTO the order request containing user ID, cart items, shipping details, etc.
     * @return the created {@link OrderResponseDTO} containing order details
     */
    OrderResponseDTO placeOrder(OrderRequestDTO requestDTO);

    /**
     * Retrieves the details of a specific order by its ID.
     *
     * @param orderId the ID of the order to retrieve
     * @return the {@link OrderResponseDTO} containing order information
     */
    OrderResponseDTO getOrderById(Long orderId);

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId the ID of the user whose orders are to be fetched
     * @return a list of {@link OrderResponseDTO}
     */
    List<OrderResponseDTO> getOrdersByUserId(Long userId);

    /**
     * Retrieves a list of all orders in the system.
     *
     * @return a list of all {@link OrderResponseDTO}
     */
    List<OrderResponseDTO> getAllOrders();

    /**
     * Cancels an order by its ID.
     *
     * @param orderId the ID of the order to be canceled
     */
    void cancelOrder(Long orderId);
}

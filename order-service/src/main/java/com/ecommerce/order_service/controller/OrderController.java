package com.ecommerce.order_service.controller;

import com.ecommerce.common_dto.dto.order.OrderRequestDTO;
import com.ecommerce.common_dto.dto.order.OrderResponseDTO;
import com.ecommerce.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for handling order-related HTTP requests.
 * Handles placing new orders, retrieving orders by ID or user, listing all orders, and canceling orders.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    /**
     * Places a new order based on the given request data.
     *
     * @param requestDTO Order request data (validated).
     * @return The created order response.
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @Valid @RequestBody OrderRequestDTO requestDTO) {
        log.info("API Call: Place new order");
        OrderResponseDTO responseDTO = orderService.placeOrder(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Retrieves an order by its unique ID.
     *
     * @param orderId The ID of the order.
     * @return The order response if found.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long orderId) {
        log.info("API Call: Get order by ID {}", orderId);
        OrderResponseDTO responseDTO = orderService.getOrderById(orderId);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Retrieves all orders associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of order responses.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByUserId(@PathVariable Long userId) {
        log.info("API Call: Get orders for userId {}", userId);
        List<OrderResponseDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all orders in the system.
     *
     * @return A list of all order responses.
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        log.info("API Call: Get all orders");
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Cancels an order by its ID.
     *
     * @param orderId The ID of the order to cancel.
     * @return A response entity with no content status.
     */
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        log.info("API Call: Cancel order with ID {}", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

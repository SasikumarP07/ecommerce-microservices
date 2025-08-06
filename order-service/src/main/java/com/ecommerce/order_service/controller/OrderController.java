package com.ecommerce.order_service.controller;

import com.ecommerce.common_dto.dto.order.OrderRequestDTO;
import com.ecommerce.common_dto.dto.order.OrderResponseDTO;
import com.ecommerce.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Place a new order",
            description = "Places a new order for the user with cart and shipping information.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Order request details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = OrderRequestDTO.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Get order by ID", description = "Retrieve a specific order using its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
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
    @Operation(summary = "Get orders by user ID", description = "Retrieve all orders for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class))))
    })
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
    @Operation(summary = "Get all orders", description = "Retrieve all orders in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All orders retrieved",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class))))
    })
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
    @Operation(summary = "Cancel order", description = "Cancel a specific order by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order canceled successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        log.info("API Call: Cancel order with ID {}", orderId);
        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}

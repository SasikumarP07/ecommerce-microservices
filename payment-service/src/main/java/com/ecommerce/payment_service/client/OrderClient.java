package com.ecommerce.payment_service.client;

import com.ecommerce.common_dto.dto.order.OrderResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the Order Service.
 * <p>
 * This interface allows the Payment Service to retrieve order details
 * (e.g., total amount, user info, order status) before processing the payment.
 * </p>
 */
@FeignClient(name = "ORDER-SERVICE") // Name as registered with Eureka
public interface OrderClient {

    /**
     * Retrieves an order by its ID from the Order Service.
     *
     * @param orderId the unique identifier of the order
     * @return the details of the specified order
     */
    @GetMapping("/orders/{orderId}")
    OrderResponseDTO getOrderById(@PathVariable("orderId") Long orderId);
}

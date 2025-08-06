package com.ecommerce.product_service.client;

import com.ecommerce.common_dto.dto.inventory.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign client interface for communicating with the Inventory Service.
 * <p>
 * This client is used by the Product Service to trigger inventory creation
 * whenever a new product is added.
 */
@FeignClient(name = "INVENTORY-SERVICE", path = "/api/inventory")
public interface InventoryClient {

    /**
     * Sends a POST request to the Inventory Service to create inventory
     * for a newly created product.
     *
     * @param requestDTO The inventory request data containing productId, quantity, etc.
     * @return InventoryResponseDTO containing the details of the created inventory.
     */
    @PostMapping
    InventoryResponseDTO createInventory(@RequestBody InventoryRequestDTO requestDTO);
}

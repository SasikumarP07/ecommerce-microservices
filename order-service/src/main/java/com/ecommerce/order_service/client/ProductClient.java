package com.ecommerce.order_service.client;

import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign client for communicating with the Product Service.
 * <p>
 * This interface is used to fetch product details by making a REST call
 * to the Product Service registered with Eureka.
 */
@FeignClient(name = "product-service")  // This name must match the application name of Product Service in Eureka
public interface ProductClient {

    /**
     * Retrieves product details from the Product Service based on the product ID.
     *
     * @param productId The unique identifier of the product to fetch.
     * @return {@link ProductResponseDTO} containing product details.
     */
    @GetMapping("/api/products/{productId}")
    ProductResponseDTO getProductById(@PathVariable("productId") Long productId);
}

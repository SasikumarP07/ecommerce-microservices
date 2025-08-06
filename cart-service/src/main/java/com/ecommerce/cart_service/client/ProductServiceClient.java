package com.ecommerce.cart_service.client;

import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Feign client for communicating with the Product Service.
 * This interface provides methods to fetch product details
 * by product ID or in bulk using a list of product IDs.
 * Feign automatically handles HTTP requests under the hood.
 */
@FeignClient(name = "PRODUCT-SERVICE", path = "/api/products")
public interface ProductServiceClient {

    /**
     * Retrieves a product by its ID from the Product Service.
     *
     * @param id the ID of the product to fetch
     * @return the product details as a ProductResponseDTO
     */
    @GetMapping("/{id}")
    ProductResponseDTO getProductById(@PathVariable("id") Long id);

    /**
     * Retrieves a list of products from the Product Service
     * based on a list of product IDs.
     *
     * @param ids the list of product IDs to fetch
     * @return a list of ProductResponseDTO objects
     */
    @PostMapping("/bulk")
    List<ProductResponseDTO> getProductsById(@RequestBody List<Long> ids);
}

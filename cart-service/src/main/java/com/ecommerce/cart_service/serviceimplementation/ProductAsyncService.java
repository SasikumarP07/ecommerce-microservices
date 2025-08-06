package com.ecommerce.cart_service.serviceimplementation;

import com.ecommerce.cart_service.client.ProductServiceClient;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service class to asynchronously fetch product details from the Product Service.
 * This allows non-blocking retrieval of product data to improve performance,
 * especially when enriching cart items with product information.
 */
@Service
@RequiredArgsConstructor
public class ProductAsyncService {

    private final ProductServiceClient productClient;

    /**
     * Asynchronously fetches a product by its ID using the Feign client.
     *
     * @param productId the ID of the product to retrieve
     * @return a CompletableFuture containing the ProductResponseDTO
     */
    @Async
    public CompletableFuture<ProductResponseDTO> fetchProduct(Long productId) {
        ProductResponseDTO product = productClient.getProductById(productId);
        return CompletableFuture.completedFuture(product);
    }
}

package com.ecommerce.product_service.mapper;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;
import com.ecommerce.product_service.entity.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

/**
 * Mapper class for converting between {@link Category} entities and their corresponding DTOs.
 * Provides synchronous and asynchronous methods for transformation.
 */
@Slf4j
public class CategoryMapper {

    /**
     * Converts a {@link Category} entity to a {@link CategoryResponseDTO}.
     *
     * @param category the entity to convert
     * @return the corresponding DTO
     */
    public static CategoryResponseDTO toDto(Category category) {
        log.debug("Converting Category entity to DTO: {}", category);
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        log.debug("Converted CategoryResponseDTO: {}", dto);
        return dto;
    }

    /**
     * Converts a {@link CategoryRequestDTO} to a {@link Category} entity.
     *
     * @param requestDTO the DTO to convert
     * @return the corresponding entity
     */
    public static Category toEntity(CategoryRequestDTO requestDTO){
        log.debug("Converting CategoryRequestDTO to entity: {}", requestDTO);
        Category category = new Category();
        category.setName(requestDTO.getName());
        category.setDescription(requestDTO.getDescription());
        log.debug("Converted Category entity: {}", category);
        return category;
    }

    /**
     * Asynchronously converts a {@link Category} entity to a {@link CategoryResponseDTO}.
     * Simulates processing delay for demonstration.
     *
     * @param category the entity to convert
     * @return a {@link CompletableFuture} containing the converted DTO
     */
    @Async
    public static CompletableFuture<CategoryResponseDTO> toDTOAsync(Category category) {
        log.debug("Asynchronously converting Category to DTO: {}", category);

        try {
            Thread.sleep(100); // Simulate processing time
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Thread interrupted during async mapping", e);
        }

        CategoryResponseDTO dto = toDto(category);
        log.debug("Asynchronously converted CategoryResponseDTO: {}", dto);
        return CompletableFuture.completedFuture(dto);
    }
}

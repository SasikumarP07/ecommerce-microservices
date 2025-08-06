package com.ecommerce.inventory_service.controller;

import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.common_dto.dto.inventory.InventoryResponseDTO;
import com.ecommerce.inventory_service.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 📦 InventoryController handles all incoming HTTP requests related to inventory management.
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(
            summary = "Create inventory record",
            description = "Creates a new inventory record for a product."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory created successfully",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class)))
    })
    @PostMapping("/create")
    public ResponseEntity<InventoryResponseDTO> createInventory(
            @RequestBody(
                    description = "Inventory creation request with product ID and initial quantity",
                    required = true,
                    content = @Content(schema = @Schema(implementation = InventoryRequestDTO.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody InventoryRequestDTO requestDTO
    ) {
        log.info("Creating inventory for productId: {}", requestDTO.getProductId());
        InventoryResponseDTO created = inventoryService.createInventory(requestDTO);
        log.info("Inventory created: {}", created);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Get inventory by productId",
            description = "Fetch inventory details for the given product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inventory retrieved",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class)))
    })
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventory(@PathVariable Long productId) {
        log.info("Fetching inventory for productId: {}", productId);
        InventoryResponseDTO inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

    @Operation(
            summary = "Update stock quantity",
            description = "Updates the stock quantity for a given product ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock updated",
                    content = @Content(schema = @Schema(implementation = InventoryResponseDTO.class)))
    })
    @Transactional
    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> updateStock(
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        log.info("Updating stock for productId: {} to quantity: {}", productId, quantity);
        InventoryResponseDTO updated = inventoryService.updateStock(productId, quantity);
        log.info("Stock updated: {}", updated);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Reduce stock quantity",
            description = "Reduces the stock quantity for a given product ID, typically during order placement."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock reduced successfully")
    })
    @Transactional
    @PostMapping("/{productId}/reduce")
    public ResponseEntity<String> reduceStock(
            @PathVariable Long productId,
            @RequestParam int quantity
    ) {
        log.info("Reducing stock for productId: {} by quantity: {}", productId, quantity);
        inventoryService.reduceStock(productId, quantity);
        log.info("Stock reduced successfully for productId: {}", productId);
        return ResponseEntity.ok("Stock reduced successfully");
    }
}

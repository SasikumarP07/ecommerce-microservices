package com.ecommerce.inventory_service.controller;

import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.common_dto.dto.inventory.InventoryResponseDTO;
import com.ecommerce.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * InventoryController handles all incoming HTTP requests related to inventory management.
 */
@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/create")
    public ResponseEntity<InventoryResponseDTO> createInventory(
            @Valid @RequestBody InventoryRequestDTO requestDTO
    ) {
        log.info("Creating inventory for productId: {}", requestDTO.getProductId());
        InventoryResponseDTO created = inventoryService.createInventory(requestDTO);
        log.info("Inventory created: {}", created);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponseDTO> getInventory(@PathVariable Long productId) {
        log.info("Fetching inventory for productId: {}", productId);
        InventoryResponseDTO inventory = inventoryService.getInventoryByProductId(productId);
        return ResponseEntity.ok(inventory);
    }

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

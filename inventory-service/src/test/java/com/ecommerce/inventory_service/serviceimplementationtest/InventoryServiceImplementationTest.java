package com.ecommerce.inventory_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.inventory.InventoryRequestDTO;
import com.ecommerce.common_dto.dto.inventory.InventoryResponseDTO;
import com.ecommerce.inventory_service.entity.Inventory;
import com.ecommerce.inventory_service.exception.ResourceNotFoundException;
import com.ecommerce.inventory_service.repository.InventoryRepository;
import com.ecommerce.inventory_service.serviceimplementation.InventoryServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link InventoryServiceImplementation}.
 * This test class uses Mockito to mock the {@link InventoryRepository} dependency and
 * validate the behavior of service methods under different scenarios, including success and failure cases.
 * It ensures:
 * - Inventory creation behaves as expected
 * - Product inventory retrieval works and handles missing data
 * - Stock updates and reductions handle both valid and invalid cases
 * Mocking tools:
 * - @Mock: Creates mock instances for dependencies
 * - @InjectMocks: Injects mocked dependencies into the service implementation
 * Author: [Your Name]
 * Date: August 2025
 */
class InventoryServiceImplementationTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImplementation inventoryService;

    private Inventory inventory;
    private InventoryRequestDTO requestDTO;

    /**
     * Initializes mock objects and creates test data before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        inventory = Inventory.builder()
                .productId(1L)
                .quantity(10)
                .build();

        requestDTO = InventoryRequestDTO.builder()
                .productId(1L)
                .quantity(10)
                .build();
    }

    /**
     * Tests successful creation of inventory using valid request data.
     */
    @Test
    void testCreateInventory() {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponseDTO response = inventoryService.createInventory(requestDTO);

        assertNotNull(response);
        assertEquals(inventory.getProductId(), response.getProductId());
        assertEquals(inventory.getQuantity(), response.getStockQuantity());
        verify(inventoryRepository, times(1)).save(any(Inventory.class));
    }

    /**
     * Tests retrieval of inventory by product ID when inventory exists.
     */
    @Test
    void testGetInventoryByProductId_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        InventoryResponseDTO response = inventoryService.getInventoryByProductId(1L);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals(10, response.getStockQuantity());
    }

    /**
     * Tests retrieval of inventory by product ID when inventory is not found.
     * Expects {@link ResourceNotFoundException}.
     */
    @Test
    void testGetInventoryByProductId_NotFound() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getInventoryByProductId(1L));
    }

    /**
     * Tests successful stock update for a valid product ID.
     */
    @Test
    void testUpdateStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        InventoryResponseDTO response = inventoryService.updateStock(1L, 20);

        assertNotNull(response);
        assertEquals(1L, response.getProductId());
        assertEquals(20, response.getStockQuantity());
    }

    /**
     * Tests stock update when product ID does not exist.
     * Expects {@link ResourceNotFoundException}.
     */
    @Test
    void testUpdateStock_NotFound() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.updateStock(1L, 15));
    }

    /**
     * Tests reducing stock when sufficient quantity is available.
     */
    @Test
    void testReduceStock_Success() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        inventoryService.reduceStock(1L, 5);

        verify(inventoryRepository, times(1)).save(any(Inventory.class));
        assertEquals(5, inventory.getQuantity());
    }

    /**
     * Tests reducing stock when requested quantity exceeds available stock.
     * Expects {@link IllegalArgumentException}.
     */
    @Test
    void testReduceStock_InsufficientQuantity() {
        inventory.setQuantity(3);
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inventory));

        assertThrows(IllegalArgumentException.class, () -> inventoryService.reduceStock(1L, 5));
    }

    /**
     * Tests reducing stock when inventory for the product does not exist.
     * Expects {@link ResourceNotFoundException}.
     */
    @Test
    void testReduceStock_InventoryNotFound() {
        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.reduceStock(1L, 1));
    }
}

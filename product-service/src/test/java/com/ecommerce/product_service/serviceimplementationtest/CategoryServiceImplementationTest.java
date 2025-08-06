package com.ecommerce.product_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.product.CategoryRequestDTO;
import com.ecommerce.common_dto.dto.product.CategoryResponseDTO;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.mapper.CategoryMapper;
import com.ecommerce.product_service.repository.CategoryRepository;
import com.ecommerce.product_service.serviceimplementation.CategoryServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CategoryServiceImplementation}.
 * These tests verify the correctness of business logic for managing categories,
 * using mocked dependencies.
 */
public class CategoryServiceImplementationTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImplementation categoryService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test creating a new category successfully.
     */
    @Test
    public void testCreateCategory() {
        CategoryRequestDTO requestDTO = new CategoryRequestDTO("Electronics", "All electronics");
        Category category = CategoryMapper.toEntity(requestDTO);
        category.setId(1L);

        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryResponseDTO response = categoryService.createCategory(requestDTO);

        assertEquals("Electronics", response.getName());
        assertEquals("All electronics", response.getDescription());
    }

    /**
     * Test retrieving all categories successfully.
     */
    @Test
    public void testGetAllCategories() {
        Category cat1 = new Category(1L, "Electronics", "Gadgets");
        Category cat2 = new Category(2L, "Books", "Reading");

        when(categoryRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        List<CategoryResponseDTO> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
    }

    /**
     * Test retrieving a category by ID when it exists.
     */
    @Test
    public void testGetCategoryById_Success() {
        Category category = new Category(1L, "Electronics", "Gadgets");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.getCategoryById(1L);

        assertEquals("Electronics", response.getName());
    }

    /**
     * Test retrieving a category by ID when it does not exist.
     */
    @Test
    public void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(1L));
    }

    /**
     * Test updating an existing category successfully.
     */
    @Test
    public void testUpdateCategory_Success() {
        Category existing = new Category(1L, "Old", "Old Desc");
        CategoryRequestDTO updatedDTO = new CategoryRequestDTO("New", "New Desc");
        Category updatedEntity = new Category(1L, "New", "New Desc");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedEntity);

        CategoryResponseDTO response = categoryService.updateCategory(1L, updatedDTO);

        assertEquals("New", response.getName());
        assertEquals("New Desc", response.getDescription());
    }

    /**
     * Test updating a category that doesn't exist.
     */
    @Test
    public void testUpdateCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                categoryService.updateCategory(1L, new CategoryRequestDTO("New", "New Desc")));
    }

    /**
     * Test deleting a category that exists.
     */
    @Test
    public void testDeleteCategory_Success() {
        Category category = new Category(1L, "ToDelete", "To be deleted");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).delete(category);

        assertDoesNotThrow(() -> categoryService.deleteCategory(1L));
        verify(categoryRepository, times(1)).delete(category);
    }

    /**
     * Test deleting a category that doesn't exist.
     */
    @Test
    public void testDeleteCategory_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }

    /**
     * Test retrieving a category by name when it exists.
     */
    @Test
    public void testGetCategoryByName_Success() {
        Category category = new Category(1L, "Fashion", "Trendy items");

        when(categoryRepository.findByName("Fashion")).thenReturn(Optional.of(category));

        CategoryResponseDTO response = categoryService.getCategoryByName("Fashion");

        assertEquals("Fashion", response.getName());
    }

    /**
     * Test retrieving a category by name when it does not exist.
     */
    @Test
    public void testGetCategoryByName_NotFound() {
        when(categoryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryByName("Unknown"));
    }
}

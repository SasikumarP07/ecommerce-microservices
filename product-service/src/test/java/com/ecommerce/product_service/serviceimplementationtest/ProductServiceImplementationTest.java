package com.ecommerce.product_service.serviceimplementationtest;

import com.ecommerce.common_dto.dto.product.ProductRequestDTO;
import com.ecommerce.common_dto.dto.product.ProductResponseDTO;
import com.ecommerce.product_service.entity.Category;
import com.ecommerce.product_service.entity.Product;
import com.ecommerce.product_service.exception.ResourceNotFoundException;
import com.ecommerce.product_service.repository.ProductRepository;
import com.ecommerce.product_service.serviceimplementation.ProductServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductServiceImplementation}.
 * Tests the core logic related to creating, updating, retrieving,
 * and deleting product records.
 */
class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImplementation productService;

    /**
     * Initializes Mockito mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * @return Sample DTO used in multiple test cases.
     */
    private ProductRequestDTO sampleDto() {
        ProductRequestDTO dto = new ProductRequestDTO();
        dto.setName("Laptop");
        dto.setDescription("Gaming laptop");
        dto.setPrice(BigDecimal.valueOf(1200));
        dto.setBrand("Asus");
        dto.setQuantityAvailable(10);
        dto.setCategoryId(1L);
        dto.setImageUrls(List.of("img1.jpg", "img2.jpg"));
        return dto;
    }

    /**
     * @return Sample Product entity used for mocking and assertions.
     */
    private Product sampleProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming laptop");
        product.setPrice(BigDecimal.valueOf(1200));
        product.setBrand("Asus");
        product.setQuantityAvailable(10);
        Category category = new Category();
        category.setId(1L);
        product.setCategory(category);
        product.setImageUrls(new ArrayList<>());
        return product;
    }

    /**
     * Test the creation of a new product.
     */
    @Test
    void testCreateProduct() {
        ProductRequestDTO dto = sampleDto();
        Product saved = sampleProduct();

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponseDTO response = productService.createProduct(dto);
        assertNotNull(response);
        assertEquals("Laptop", response.getName());
    }

    /**
     * Test fetching a product by its ID when it exists.
     */
    @Test
    void testGetProductById_Success() {
        Product product = sampleProduct();
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.getProductById(1L);
        assertEquals("Laptop", response.getName());
    }

    /**
     * Test fetching a product by ID when it does not exist.
     */
    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    /**
     * Test deleting a product that exists.
     */
    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(() -> productService.deleteProduct(1L));
    }

    /**
     * Test deleting a product that does not exist.
     */
    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(1L));
    }

    /**
     * Test paginated retrieval of all products.
     */
    @Test
    void testGetAllProductsPaginated() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Product> products = List.of(sampleProduct());
        Page<Product> productPage = new PageImpl<>(products);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponseDTO> result = productService.getAllProductsPaginated(0, 2);
        assertEquals(1, result.getContent().size());
    }

    /**
     * Test updating an existing product successfully.
     */
    @Test
    void testUpdateProduct_Success() {
        ProductRequestDTO dto = sampleDto();
        Product existing = sampleProduct();

        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenReturn(existing);

        ProductResponseDTO response = productService.updateProduct(1L, dto);
        assertEquals("Laptop", response.getName());
        verify(productRepository).save(any(Product.class));
    }

    /**
     * Test retrieval of top 10 latest products.
     */
    @Test
    void testGetTop10LatestProducts() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        List<Product> products = List.of(sampleProduct());
        Page<Product> page = new PageImpl<>(products);

        when(productRepository.findAll(pageable)).thenReturn(page);

        List<ProductResponseDTO> result = productService.getTop10LatestProducts();
        assertEquals(1, result.size());
    }

    /**
     * Test suggestion of products for a user (e.g., recently added items).
     */
    @Test
    void testGetSuggestedProductsForUser() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        List<Product> products = List.of(sampleProduct());
        Page<Product> page = new PageImpl<>(products);

        when(productRepository.findAll(pageable)).thenReturn(page);

        List<ProductResponseDTO> result = productService.getSuggestedProductsForUser(1L);
        assertEquals(1, result.size());
    }
}

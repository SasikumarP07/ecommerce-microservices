package com.ecommerce.product_service.repository;

import com.ecommerce.product_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Category} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations, as well as
 * custom query methods for interacting with the Category table.
 * </p>
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Checks whether a category with the given name already exists.
     *
     * @param name the name of the category
     * @return {@code true} if a category with the specified name exists, otherwise {@code false}
     */
    boolean existsByName(String name);

    /**
     * Finds a category by its name.
     *
     * @param name the name of the category
     * @return an {@link Optional} containing the {@link Category} if found, otherwise empty
     */
    Optional<Category> findByName(String name);
}

package com.ecommerce.notification_service.repository;

import com.ecommerce.notification_service.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Notification} entities.
 *
 * <p>This interface extends {@link JpaRepository}, providing CRUD operations
 * and pagination support for the {@code Notification} entity.</p>
 *
 * <p>Spring Data JPA automatically provides the implementation at runtime.</p>
 *
 * @author YourName
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}

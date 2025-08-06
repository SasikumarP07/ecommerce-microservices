package com.ecommerce.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a user entity in the e-commerce system.
 * Stores user details such as name, phone, email, password, and associated addresses.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    /**
     * Unique identifier for the user.
     */
    @Id
    private Long id;

    /**
     * Name of the user.
     */
    private String name;

    /**
     * Phone number of the user.
     */
    private String phone;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Encrypted password of the user.
     */
    private String password;

    /**
     * Role assigned to the user (e.g., ROLE_USER, ROLE_ADMIN).
     */
    private String role;

    /**
     * List of addresses associated with the user.
     * This is a one-to-many relationship.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Address> addresses;
}

package com.ecommerce.user_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an address associated with a user in the e-commerce system.
 * Each user can have multiple addresses (One-to-Many relationship).
 */
@Entity
@Table(name = "addresses")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    /**
     * Unique identifier for the address.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Door number of the address.
     */
    private long doorNum;

    /**
     * Street name of the address.
     */
    private String street;

    /**
     * City of the address.
     */
    private String city;

    /**
     * State of the address.
     */
    private String state;

    /**
     * PIN/ZIP code of the address.
     */
    private String pinCode;

    /**
     * Country of the address.
     */
    private String country;

    /**
     * The user associated with this address.
     * Many addresses can belong to a single user.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

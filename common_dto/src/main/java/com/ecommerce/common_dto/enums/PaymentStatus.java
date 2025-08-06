package com.ecommerce.common_dto.enums;

/**
 * Enum representing the status of a payment.
 * <p>
 * Indicates the current state of the payment transaction.
 * </p>
 */
public enum PaymentStatus {

    /** Payment is initiated but not yet completed. */
    PENDING,

    /** Payment completed successfully. */
    SUCCESS,

    /** Payment failed due to an error or cancellation. */
    FAILED
}

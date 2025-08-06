package com.ecommerce.common_dto.enums;

/**
 * Enum representing various supported payment methods.
 * <p>
 * Used to specify how a user chooses to make a payment.
 * </p>
 */
public enum PaymentMethod {

    /** Payment made using a credit card. */
    CREDIT_CARD,

    /** Payment made using a debit card. */
    DEBIT_CARD,

    /** Payment made via net banking. */
    NET_BANKING,

    /** Payment made through UPI (Unified Payments Interface). */
    UPI,

    /** Payment to be collected at the time of delivery. */
    CASH_ON_DELIVERY
}


package smart_food_delivery.domain;

/**
 * Represents the different statuses of an order
 * in the Smart Food Delivery Management System.
 *
 * These statuses describe the order lifecycle
 * from placement to successful delivery or cancellation.
 */
public enum OrderStatus {

    /**
     * The order has been placed and is waiting
     * for confirmation.
     */
    PENDING,

    /**
     * The order has been confirmed by the system.
     */
    CONFIRMED,

    /**
     * The restaurant is preparing the order.
     */
    PREPARING,

    /**
     * The order is prepared and ready for delivery.
     */
    READY,

    /**
     * The order is currently being delivered
     * to the customer.
     */
    OUT_FOR_DELIVERY,

    /**
     * The order has been successfully delivered.
     */
    DELIVERED,

    /**
     * The order has been cancelled.
     */
    CANCELLED
}

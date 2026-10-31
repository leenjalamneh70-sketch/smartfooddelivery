
package smart_food_delivery.service;

/**
 * Observer interface for receiving notifications
 * about events in the Smart Food Delivery System.
 *
 * Implementations receive messages whenever
 * the delivery service publishes an event.
 */
public interface OrderObserver {

    /**
     * Receives an order-related notification.
     *
     * @param message the notification message
     */
    void update(String message);
}

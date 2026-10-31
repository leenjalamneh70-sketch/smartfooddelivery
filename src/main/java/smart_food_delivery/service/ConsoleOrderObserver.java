
package smart_food_delivery.service;

/**
 * Console implementation of the OrderObserver interface.
 *
 * Receives order-related events and displays
 * notification messages in the console.
 */
public class ConsoleOrderObserver implements OrderObserver {

    /**
     * Handles an order notification.
     *
     * @param message the notification message
     */
    @Override
    public void update(String message) {

        System.out.println("Notification: " + message);
    }
}

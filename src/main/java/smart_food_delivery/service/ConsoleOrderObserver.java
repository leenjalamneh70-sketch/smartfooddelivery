
package smart_food_delivery.service;

/**
 * Console implementation of the OrderObserver interface.
 *
 * Receives order-related events and displays
 * notification messages in the console.
 *
 * This class is part of the Observer Design Pattern
 * used in the Smart Food Delivery Management System.
 */
public class ConsoleOrderObserver implements OrderObserver {

    /**
     * Creates a console-based order observer.
     *
     * The observer receives notifications about
     * order-related events and displays them
     * in the standard output console.
     */
    public ConsoleOrderObserver() {
    }

    /**
     * Handles an order notification.
     *
     * Prints the received notification message
     * to the console.
     *
     * @param message the notification message
     */
    @Override
    public void update(String message) {

        System.out.println("Notification: " + message);
    }
}

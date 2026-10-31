
package smart_food_delivery.service;

/**
 * Provides a console-based implementation of
 * the NotificationService interface.
 *
 * Displays notification messages in the console
 * for the Smart Food Delivery Management System.
 */
public class ConsoleNotificationService implements NotificationService {

    /**
     * Sends a notification by printing the message
     * to the console.
     *
     * @param message the notification message to display
     */
    @Override
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}


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
     * Creates a console notification service.
     *
     * This service displays notification messages
     * in the standard output console.
     */
    public ConsoleNotificationService() {
    }

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

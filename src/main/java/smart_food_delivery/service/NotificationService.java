
package smart_food_delivery.service;

/**
 * Defines the notification service interface
 * in the Smart Food Delivery Management System.
 *
 * This interface provides a common method for
 * sending notifications to users.
 *
 * Different implementations can provide different
 * notification methods, such as console messages.
 */
public interface NotificationService {

    /**
     * Sends a notification message.
     *
     * @param message the notification message to send
     */
    void sendNotification(String message);

}

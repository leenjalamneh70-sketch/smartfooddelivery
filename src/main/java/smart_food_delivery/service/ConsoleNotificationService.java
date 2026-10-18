
package smart_food_delivery.service;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message) {
        System.out.println("Notification: " + message);
    }
}

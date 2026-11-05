
package smart_food_delivery.service;

/** Receives driver-facing order notifications. */
public class DriverOrderObserver implements OrderObserver {

    @Override
    public void update(String message) {
        System.out.println("Driver notification: " + message);
    }
}


package smart_food_delivery.service;

/** Receives customer-facing order and delivery notifications. */
public class CustomerOrderObserver implements OrderObserver {

    @Override
    public void update(String message) {
        System.out.println("Customer notification: " + message);
    }
}

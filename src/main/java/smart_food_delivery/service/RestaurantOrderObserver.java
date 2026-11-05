
package smart_food_delivery.service;

/** Receives restaurant-facing order notifications. */
public class RestaurantOrderObserver implements OrderObserver {

    @Override
    public void update(String message) {
        System.out.println("Restaurant notification: " + message);
    }
}

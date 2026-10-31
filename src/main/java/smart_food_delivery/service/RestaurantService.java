
package smart_food_delivery.service;

import java.util.ArrayList;
import java.util.List;

import smart_food_delivery.domain.Restaurant;
import smart_food_delivery.domain.MenuItem;

/**
 * Provides restaurant-related services in the
 * Smart Food Delivery Management System.
 *
 * This class allows filtering restaurants based
 * on their availability and retrieving available
 * menu items from open restaurants.
 */
public class RestaurantService {

    /**
     * Returns a list of restaurants that are currently open.
     *
     * Null restaurants and closed restaurants are excluded.
     * If the provided list is null, an empty list is returned.
     *
     * @param restaurants the list of restaurants to check
     * @return a new list containing only open restaurants
     */
    public List<Restaurant> getOpenRestaurants(List<Restaurant> restaurants) {

        List<Restaurant> result = new ArrayList<>();

        if (restaurants == null) {
            return result;
        }

        for (Restaurant r : restaurants) {
            if (r != null && r.isOpen()) {
                result.add(r);
            }
        }

        return result;
    }

    /**
     * Returns the available menu items of an open restaurant.
     *
     * Unavailable and null menu items are excluded.
     * If the restaurant is null or closed, an empty
     * list is returned.
     *
     * @param restaurant the restaurant whose menu is requested
     * @return a new list containing only available menu items
     */
    public List<MenuItem> getAvailableMenu(Restaurant restaurant) {

        List<MenuItem> result = new ArrayList<>();

        if (restaurant == null || !restaurant.isOpen()) {
            return result;
        }

        for (MenuItem item : restaurant.getMenu()) {
            if (item != null && item.isAvailable()) {
                result.add(item);
            }
        }

        return result;
    }
}

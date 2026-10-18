
package smart_food_delivery.service;

import java.util.ArrayList;
import java.util.List;

import smart_food_delivery.domain.Restaurant;
import smart_food_delivery.domain.MenuItem;

public class RestaurantService {

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

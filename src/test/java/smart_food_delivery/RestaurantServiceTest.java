
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.RestaurantService;

class RestaurantServiceTest {

    @Test
    void testOnlyOpenRestaurantsDisplayed() {

        Restaurant open = new Restaurant("Pizza House", true, 20, "City");
        Restaurant closed = new Restaurant("Burger House", false, 15, "City");

        RestaurantService service = new RestaurantService();

        assertEquals(1, service.getOpenRestaurants(
            Arrays.asList(open, closed)).size());

        assertEquals(open, service.getOpenRestaurants(
            Arrays.asList(open, closed)).get(0));
    }

    @Test
    void testOnlyAvailableMenuItemsDisplayed() {

        Restaurant restaurant =
            new Restaurant("Pizza House", true, 20, "City");

        restaurant.addMenuItem(new MenuItem("Pizza", 30, true));
        restaurant.addMenuItem(new MenuItem("Burger", 20, false));

        RestaurantService service = new RestaurantService();

        assertEquals(1, service.getAvailableMenu(restaurant).size());

        assertEquals("Pizza",
            service.getAvailableMenu(restaurant).get(0).getName());

        assertEquals(30,
            service.getAvailableMenu(restaurant).get(0).getPrice());
    }
}

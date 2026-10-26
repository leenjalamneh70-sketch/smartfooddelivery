
package smart_food_delivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a restaurant in the Smart Food Delivery
 * Management System.
 *
 * Stores restaurant information, including its name,
 * availability, minimum order value, delivery zone,
 * and list of menu items.
 */
public class Restaurant {

    private String name;
    private boolean open;
    private double minimumOrder;
    private String deliveryZone;
    private List<MenuItem> menu;

    /**
     * Creates a new restaurant.
     *
     * @param name the restaurant's name
     * @param open whether the restaurant is open
     * @param minimumOrder the minimum order value
     * @param deliveryZone the restaurant's delivery area
     */
    public Restaurant(String name, boolean open,
                      double minimumOrder, String deliveryZone) {
        this.name = name;
        this.open = open;
        this.minimumOrder = minimumOrder;
        this.deliveryZone = deliveryZone;
        this.menu = new ArrayList<>();
    }

    /**
     * Returns the restaurant's name.
     *
     * @return the restaurant name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether the restaurant is open.
     *
     * @return true if the restaurant is open,
     *         otherwise false
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Returns the minimum order value.
     *
     * @return the minimum order value
     */
    public double getMinimumOrder() {
        return minimumOrder;
    }

    /**
     * Returns the restaurant's delivery zone.
     *
     * @return the delivery zone
     */
    public String getDeliveryZone() {
        return deliveryZone;
    }

    /**
     * Adds a menu item to the restaurant's menu.
     *
     * Null items are ignored.
     *
     * @param item the menu item to add
     */
    public void addMenuItem(MenuItem item) {
        if (item != null) {
            menu.add(item);
        }
    }

    /**
     * Returns a copy of the restaurant's menu.
     *
     * @return a new list containing the menu items
     */
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }
}


package smart_food_delivery.domain;

/**
 * Represents a food item on a restaurant's menu
 * in the Smart Food Delivery Management System.
 *
 * Stores the item's name, price, and availability.
 */
public class MenuItem {

    private String name;
    private double price;
    private boolean available;

    /**
     * Creates a new menu item.
     *
     * @param name the name of the menu item
     * @param price the price of the menu item
     * @param available whether the item is available
     */
    public MenuItem(String name, double price, boolean available) {
        this.name = name;
        this.price = price;
        this.available = available;
    }

    /**
     * Returns the name of the menu item.
     *
     * @return the item name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the price of the menu item.
     *
     * @return the item price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Checks whether the menu item is available.
     *
     * @return true if the item is available,
     *         otherwise false
     */
    public boolean isAvailable() {
        return available;
    }
}

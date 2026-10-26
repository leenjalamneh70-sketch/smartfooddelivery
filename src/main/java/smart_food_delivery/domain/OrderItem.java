
package smart_food_delivery.domain;

/**
 * Represents an item included in a customer's order
 * in the Smart Food Delivery Management System.
 *
 * Stores the selected menu item and its quantity,
 * and calculates the subtotal for that item.
 */
public class OrderItem {

    private MenuItem item;
    private int quantity;

    /**
     * Creates a new order item with a specified
     * menu item and quantity.
     *
     * @param item the selected menu item
     * @param quantity the number of units ordered
     */
    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Returns the selected menu item.
     *
     * @return the menu item
     */
    public MenuItem getItem() {
        return item;
    }

    /**
     * Returns the quantity of the ordered item.
     *
     * @return the quantity ordered
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Calculates the subtotal of the order item
     * by multiplying its price by the quantity.
     *
     * @return the subtotal for this order item
     */
    public double getSubtotal() {
        return item.getPrice() * quantity;
    }
}

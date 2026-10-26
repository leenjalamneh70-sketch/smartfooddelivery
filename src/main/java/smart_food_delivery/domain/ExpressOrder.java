
package smart_food_delivery.domain;

import java.util.List;

/**
 * Represents an express order in the Smart Food Delivery System.
 * An express order includes an additional express delivery fee.
 *
 * This class extends the Order class and inherits
 * its basic order information and behavior.
 */
public class ExpressOrder extends Order {

    private final double expressFee;

    /**
     * Creates a new express delivery order.
     *
     * @param id the unique order ID
     * @param restaurant the restaurant preparing the order
     * @param items the items included in the order
     * @param deliveryAddress the delivery address
     * @param deliveryZone the delivery zone
     * @param expressFee the additional express delivery fee
     * @throws IllegalArgumentException if the express fee
     *         is not a positive finite number
     */
    public ExpressOrder(int id, Restaurant restaurant,
                        List<OrderItem> items,
                        String deliveryAddress,
                        String deliveryZone,
                        double expressFee) {

        super(id, restaurant, items, deliveryAddress, deliveryZone);

        if (!Double.isFinite(expressFee) || expressFee <= 0) {
            throw new IllegalArgumentException("Invalid express fee");
        }

        this.expressFee = expressFee;
    }

    /**
     * Returns the additional express delivery fee.
     *
     * @return the express delivery fee
     */
    public double getExpressFee() {
        return expressFee;
    }
}

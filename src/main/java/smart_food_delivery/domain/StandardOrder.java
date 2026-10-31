
package smart_food_delivery.domain;

import java.util.List;

/**
 * Represents a standard delivery order in the
 * Smart Food Delivery Management System.
 *
 * This class extends the Order class and follows
 * the normal order and delivery policies without
 * adding any express delivery fees.
 */
public class StandardOrder extends Order {

    /**
     * Creates a new standard delivery order.
     *
     * @param id the unique ID of the order
     * @param restaurant the restaurant preparing the order
     * @param items the list of items included in the order
     * @param deliveryAddress the delivery address
     * @param deliveryZone the delivery zone
     */
    public StandardOrder(int id, Restaurant restaurant,
                         List<OrderItem> items,
                         String deliveryAddress,
                         String deliveryZone) {

        super(id, restaurant, items, deliveryAddress, deliveryZone);
    }
}

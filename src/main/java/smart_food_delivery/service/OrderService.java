
package smart_food_delivery.service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import smart_food_delivery.domain.*;

/**
 * Manages food orders in the Smart Food Delivery
 * Management System.
 *
 * This service creates standard, express, and scheduled
 * orders, validates order information, assigns unique
 * order IDs, and allows pending orders to be cancelled.
 */
public class OrderService {

    private List<Order> orders = new ArrayList<>();
    private int nextId = 1;

    /**
     * Places a standard order using the original
     * order creation method.
     *
     * This method is kept for compatibility with
     * existing parts of the system.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @return the newly created standard order
     * @throws IllegalArgumentException if the order
     *         information is invalid
     */
    public Order placeOrder(Restaurant restaurant,
                            List<OrderItem> items,
                            String address,
                            String zone) {

        return placeStandardOrder(restaurant, items, address, zone);
    }

    /**
     * Creates and saves a standard delivery order.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @return the newly created standard order
     * @throws IllegalArgumentException if the order
     *         information is invalid
     */
    public StandardOrder placeStandardOrder(
            Restaurant restaurant, List<OrderItem> items,
            String address, String zone) {

        validateOrder(restaurant, items, address, zone);

        StandardOrder order = new StandardOrder(
            nextId, restaurant, items, address, zone);

        saveOrder(order);
        return order;
    }

    /**
     * Creates and saves an express delivery order.
     *
     * Express orders include an additional express fee.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @param expressFee the additional express delivery fee
     * @return the newly created express order
     * @throws IllegalArgumentException if the order
     *         information or express fee is invalid
     */
    public ExpressOrder placeExpressOrder(
            Restaurant restaurant, List<OrderItem> items,
            String address, String zone, double expressFee) {

        validateOrder(restaurant, items, address, zone);

        ExpressOrder order = new ExpressOrder(
            nextId, restaurant, items, address, zone, expressFee);

        saveOrder(order);
        return order;
    }

    /**
     * Creates a scheduled order using the system clock.
     *
     * The scheduled delivery time must be in the future.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @param scheduledTime the requested delivery date and time
     * @return the newly created scheduled order
     * @throws IllegalArgumentException if the order data
     *         is invalid or the scheduled time is not in the future
     */
    public ScheduledOrder placeScheduledOrder(
            Restaurant restaurant, List<OrderItem> items,
            String address, String zone,
            LocalDateTime scheduledTime) {

        return placeScheduledOrder(
            restaurant, items, address, zone,
            scheduledTime, Clock.systemDefaultZone());
    }

    /**
     * Creates a scheduled order using a specified clock.
     *
     * Providing a clock allows deterministic testing
     * of future delivery times.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @param scheduledTime the requested delivery date and time
     * @param clock the clock used for time validation
     * @return the newly created scheduled order
     * @throws IllegalArgumentException if the order data,
     *         scheduled time, or clock is invalid
     */
    public ScheduledOrder placeScheduledOrder(
            Restaurant restaurant, List<OrderItem> items,
            String address, String zone,
            LocalDateTime scheduledTime, Clock clock) {

        validateOrder(restaurant, items, address, zone);

        ScheduledOrder order = new ScheduledOrder(
            nextId, restaurant, items, address, zone,
            scheduledTime, clock);

        saveOrder(order);
        return order;
    }

    /**
     * Saves an order and increments the next order ID.
     *
     * @param order the order to save
     */
    private void saveOrder(Order order) {
        orders.add(order);
        nextId++;
    }

    /**
     * Validates the restaurant, ordered items,
     * delivery address, and delivery zone.
     *
     * Checks restaurant availability, item quantities,
     * menu membership, item availability, prices,
     * and the minimum order value.
     *
     * @param restaurant the selected restaurant
     * @param items the ordered items
     * @param address the delivery address
     * @param zone the delivery zone
     * @throws IllegalArgumentException if any order
     *         information fails validation
     */
    private void validateOrder(Restaurant restaurant,
                               List<OrderItem> items,
                               String address, String zone) {

        if (restaurant == null || !restaurant.isOpen()) {
            throw new IllegalArgumentException(
                "Restaurant is closed or invalid");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException(
                "Order cannot be empty");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Invalid delivery address");
        }

        if (zone == null ||
            !restaurant.getDeliveryZone()
                .equalsIgnoreCase(zone.trim())) {
            throw new IllegalArgumentException(
                "Outside delivery zone");
        }

        for (OrderItem orderItem : items) {

            if (orderItem == null || orderItem.getItem() == null) {
                throw new IllegalArgumentException("Invalid item");
            }

            if (orderItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }

            if (!orderItem.getItem().isAvailable()) {
                throw new IllegalArgumentException("Unavailable item");
            }

            if (!restaurant.getMenu().contains(orderItem.getItem())) {
                throw new IllegalArgumentException(
                    "Item does not belong to restaurant");
            }

            if (orderItem.getItem().getPrice() < 0) {
                throw new IllegalArgumentException("Invalid item price");
            }
        }

        double total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        if (total < restaurant.getMinimumOrder()) {
            throw new IllegalArgumentException(
                "Order is below minimum order value");
        }
    }

    /**
     * Cancels an order if it belongs to this service
     * and is still pending.
     *
     * @param order the order to cancel
     * @return true if the order was cancelled,
     *         otherwise false
     */
    public boolean cancelOrder(Order order) {

        if (order == null || !orders.contains(order)) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        order.setStatus(OrderStatus.CANCELLED);
        return true;
    }

    /**
     * Returns a copy of the stored orders list.
     *
     * @return a new list containing all stored orders
     */
    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}

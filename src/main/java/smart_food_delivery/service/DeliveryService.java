
package smart_food_delivery.service;

import java.util.ArrayList;
import java.util.List;

import smart_food_delivery.domain.*;

/**
 * Manages driver assignment and order delivery status
 * in the Smart Food Delivery Management System.
 *
 * Uses a driver selection strategy to choose an eligible
 * driver based on delivery distance and order size.
 *
 * Implements the Observer Pattern by allowing observers
 * to register and receive notifications when a driver
 * is assigned or an order status changes.
 *
 * Maintains the existing NotificationService to preserve
 * compatibility with the original notification system.
 */
public class DeliveryService {

    private DriverSelectionStrategy strategy;
    private NotificationService notificationService;

    // Registered observers for order-related events.
    private final List<OrderObserver> observers = new ArrayList<>();

    /**
     * Creates a delivery service with a driver selection
     * strategy and a notification service.
     *
     * @param strategy strategy used to select drivers
     * @param notificationService service used for notifications
     * @throws IllegalArgumentException if a dependency is null
     */
    public DeliveryService(
            DriverSelectionStrategy strategy,
            NotificationService notificationService) {

        if (strategy == null || notificationService == null) {
            throw new IllegalArgumentException(
                "Services cannot be null");
        }

        this.strategy = strategy;
        this.notificationService = notificationService;

        // Default observers for the three required notification audiences.
        addObserver(new CustomerOrderObserver());
        addObserver(new RestaurantOrderObserver());
        addObserver(new DriverOrderObserver());
    }

    /**
     * Registers an observer for order-related events.
     *
     * The same observer cannot be registered twice.
     *
     * @param observer observer to register
     * @throws IllegalArgumentException if observer is null
     */
    public void addObserver(OrderObserver observer) {

        if (observer == null) {
            throw new IllegalArgumentException(
                "Observer cannot be null");
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the registered list.
     *
     * @param observer observer to remove
     */
    public void removeObserver(OrderObserver observer) {

        observers.remove(observer);
    }

    /**
     * Sends an event notification to all registered
     * observers.
     *
     * A copy of the list is used to prevent modification
     * problems during notification.
     *
     * @param message notification message
     */
    private void notifyObservers(String message) {

        for (OrderObserver observer : new ArrayList<>(observers)) {
            observer.update(message);
        }
    }

    /**
     * Assigns an eligible driver to an order.
     *
     * Calculates the total quantity of ordered items
     * and checks driver eligibility.
     *
     * Only eligible drivers are passed to the selection
     * strategy.
     *
     * After successful assignment, the driver becomes
     * unavailable and all observers are notified.
     *
     * @param order order requiring a delivery driver
     * @param drivers available driver candidates
     * @return true if a driver was assigned, otherwise false
     */
    public boolean assignDriver(Order order, List<Driver> drivers) {

        if (order == null || drivers == null
                || order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getAssignedDriver() != null) {
            return false;
        }

        double distance = order.getDeliveryDistance();

        // Calculate the total quantity of ordered items.
        int orderSize = 0;

        for (OrderItem item : order.getItems()) {
            orderSize += item.getQuantity();
        }

        List<Driver> eligibleDrivers = new ArrayList<>();

        for (Driver driver : drivers) {

            if (driver != null
                    && driver.canDeliver(distance, orderSize)) {

                eligibleDrivers.add(driver);
            }
        }

        if (eligibleDrivers.isEmpty()) {
            return false;
        }

        Driver driver = strategy.selectDriver(eligibleDrivers);

        if (driver == null
                || !eligibleDrivers.contains(driver)
                || !driver.canDeliver(distance, orderSize)) {
            return false;
        }

        order.setAssignedDriver(driver);
        driver.setAvailable(false);

        // Notify the existing notification service
        // and all registered observers.
        String message = "Driver " + driver.getName()
                + " assigned to order #" + order.getId();

        notificationService.sendNotification(message);
        notifyObservers(message);

        return true;
    }

    /**
     * Updates the status of an order according to
     * the allowed delivery workflow.
     *
     * Valid transitions:
     *
     * PENDING -> CONFIRMED
     * CONFIRMED -> PREPARING
     * PREPARING -> READY
     * READY -> OUT_FOR_DELIVERY
     * OUT_FOR_DELIVERY -> DELIVERED
     *
     * An order must have an assigned driver before
     * moving to OUT_FOR_DELIVERY.
     *
     * A notification is sent after every successful
     * status change.
     *
     * When an order is delivered, the assigned driver
     * becomes available again.
     *
     * @param order order to update
     * @param newStatus requested order status
     * @throws IllegalArgumentException if an argument is null
     * @throws IllegalStateException if the transition is invalid
     */
    public void updateOrderStatus(
            Order order,
            OrderStatus newStatus) {

        if (order == null || newStatus == null) {
            throw new IllegalArgumentException(
                "Invalid order or status");
        }

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.CANCELLED
                || current == OrderStatus.DELIVERED) {

            throw new IllegalStateException(
                "Order cannot be modified");
        }

        boolean valid = false;

        switch (current) {

            case PENDING:
                valid = newStatus == OrderStatus.CONFIRMED;
                break;

            case CONFIRMED:
                valid = newStatus == OrderStatus.PREPARING;
                break;

            case PREPARING:
                valid = newStatus == OrderStatus.READY;
                break;

            case READY:
                valid = newStatus == OrderStatus.OUT_FOR_DELIVERY
                        && order.getAssignedDriver() != null;
                break;

            case OUT_FOR_DELIVERY:
                valid = newStatus == OrderStatus.DELIVERED;
                break;

            default:
                valid = false;
        }

        if (!valid) {
            throw new IllegalStateException(
                "Invalid status transition");
        }

        order.setStatus(newStatus);

        // Notify the existing notification service
        // and all registered observers.
        String message = "Order #" + order.getId()
                + " status changed to " + newStatus;

        notificationService.sendNotification(message);
        notifyObservers(message);

        // Make the driver available after delivery.
        if (newStatus == OrderStatus.DELIVERED) {

            if (order.getAssignedDriver() != null) {
                order.getAssignedDriver().setAvailable(true);
            }
        }
    }
}

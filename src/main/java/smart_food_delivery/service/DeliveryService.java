
package smart_food_delivery.service;

import java.util.ArrayList;
import java.util.List;
import smart_food_delivery.domain.*;

/**
 * Manages driver assignment and order delivery status
 * in the Smart Food Delivery Management System.
 *
 * This service uses a driver selection strategy to
 * choose an eligible driver based on the order's
 * delivery distance and total item quantity.
 *
 * It also validates order status transitions and
 * sends notifications when a driver is assigned
 * or an order status changes.
 */
public class DeliveryService {

    private DriverSelectionStrategy strategy;
    private NotificationService notificationService;

    /**
     * Creates a delivery service with the required
     * driver selection strategy and notification service.
     *
     * @param strategy the strategy used to select a driver
     * @param notificationService the service used to send notifications
     * @throws IllegalArgumentException if either dependency is null
     */
    public DeliveryService(DriverSelectionStrategy strategy,
                           NotificationService notificationService) {

        if (strategy == null || notificationService == null) {
            throw new IllegalArgumentException(
                "Services cannot be null");
        }

        this.strategy = strategy;
        this.notificationService = notificationService;
    }

    /**
     * Assigns an eligible driver to an order.
     *
     * The method calculates the total quantity of ordered
     * items and checks each driver's delivery eligibility.
     *
     * Only eligible drivers are passed to the selection
     * strategy. After assignment, the driver becomes
     * unavailable and a notification is sent.
     *
     * @param order the order requiring a delivery driver
     * @param drivers the list of available driver candidates
     * @return true if a driver was successfully assigned,
     *         otherwise false
     */
    public boolean assignDriver(Order order, List<Driver> drivers) {

        if (order == null || drivers == null
                || order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getAssignedDriver() != null) {
            return false;
        }

        double distance = order.getDeliveryDistance();

        // Total quantity represents order size
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

        notificationService.sendNotification(
            "Driver " + driver.getName()
            + " assigned to order #" + order.getId()
        );

        return true;
    }

    /**
     * Updates the status of an order according to
     * the allowed delivery workflow.
     *
     * Valid status transitions are:
     *
     * PENDING to CONFIRMED
     * CONFIRMED to PREPARING
     * PREPARING to READY
     * READY to OUT_FOR_DELIVERY
     * OUT_FOR_DELIVERY to DELIVERED
     *
     * An order must have an assigned driver before
     * moving to OUT_FOR_DELIVERY.
     *
     * A notification is sent after each successful
     * status update. When an order is delivered,
     * its assigned driver becomes available again.
     *
     * @param order the order to update
     * @param newStatus the requested new order status
     * @throws IllegalArgumentException if the order
     *         or new status is null
     * @throws IllegalStateException if the order is
     *         completed, cancelled, or the transition
     *         is not allowed
     */
    public void updateOrderStatus(Order order, OrderStatus newStatus) {

        if (order == null || newStatus == null) {
            throw new IllegalArgumentException(
                "Invalid order or status");
        }

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.CANCELLED ||
            current == OrderStatus.DELIVERED) {
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

        notificationService.sendNotification(
            "Order #" + order.getId()
            + " status changed to " + newStatus
        );

        if (newStatus == OrderStatus.DELIVERED) {
            if (order.getAssignedDriver() != null) {
                order.getAssignedDriver().setAvailable(true);
            }
        }
    }
}

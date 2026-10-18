
package smart_food_delivery.service;

import java.util.List;
import smart_food_delivery.domain.*;

public class DeliveryService {

    private DriverSelectionStrategy strategy;
    private NotificationService notificationService;

    public DeliveryService(DriverSelectionStrategy strategy,
                           NotificationService notificationService) {

        if (strategy == null || notificationService == null) {
            throw new IllegalArgumentException("Services cannot be null");
        }

        this.strategy = strategy;
        this.notificationService = notificationService;
    }

    public boolean assignDriver(Order order, List<Driver> drivers) {

        if (order == null || order.getStatus() == OrderStatus.CANCELLED
                || order.getStatus() == OrderStatus.DELIVERED
                || order.getAssignedDriver() != null) {
            return false;
        }

        Driver driver = strategy.selectDriver(drivers);

        if (driver == null || !driver.isAvailable()) {
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

    public void updateOrderStatus(Order order, OrderStatus newStatus) {

        if (order == null || newStatus == null) {
            throw new IllegalArgumentException("Invalid order or status");
        }

        OrderStatus current = order.getStatus();

        if (current == OrderStatus.CANCELLED ||
            current == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Order cannot be modified");
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
            throw new IllegalStateException("Invalid status transition");
        }

        order.setStatus(newStatus);

        notificationService.sendNotification(
            "Order #" + order.getId()
            + " status changed to " + newStatus
        );

        if (newStatus == OrderStatus.DELIVERED) {
            order.getAssignedDriver().setAvailable(true);
        }
    }
}

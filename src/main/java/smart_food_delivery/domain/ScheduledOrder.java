
package smart_food_delivery.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a scheduled delivery order in the
 * Smart Food Delivery Management System.
 *
 * A scheduled order allows the customer to select
 * a future date and time for delivery.
 *
 * This class extends the Order class and ensures
 * that the scheduled delivery time is in the future.
 */
public class ScheduledOrder extends Order {

    private final LocalDateTime scheduledTime;

    /**
     * Creates a scheduled order using the system clock.
     *
     * @param id the unique ID of the order
     * @param restaurant the restaurant preparing the order
     * @param items the list of items included in the order
     * @param deliveryAddress the delivery address
     * @param deliveryZone the delivery zone
     * @param scheduledTime the requested future delivery time
     * @throws IllegalArgumentException if the scheduled
     *         time is null or not in the future
     */
    public ScheduledOrder(int id, Restaurant restaurant,
                          List<OrderItem> items,
                          String deliveryAddress,
                          String deliveryZone,
                          LocalDateTime scheduledTime) {

        this(id, restaurant, items, deliveryAddress,
             deliveryZone, scheduledTime, Clock.systemDefaultZone());
    }

    /**
     * Creates a scheduled order using a specified clock.
     *
     * The clock can be provided to control the current
     * time during testing.
     *
     * @param id the unique ID of the order
     * @param restaurant the restaurant preparing the order
     * @param items the list of items included in the order
     * @param deliveryAddress the delivery address
     * @param deliveryZone the delivery zone
     * @param scheduledTime the requested future delivery time
     * @param clock the clock used to validate the scheduled time
     * @throws IllegalArgumentException if the clock is null
     *         or the scheduled time is null or not in the future
     */
    public ScheduledOrder(int id, Restaurant restaurant,
                          List<OrderItem> items,
                          String deliveryAddress,
                          String deliveryZone,
                          LocalDateTime scheduledTime,
                          Clock clock) {

        super(id, restaurant, items, deliveryAddress, deliveryZone);

        if (clock == null || scheduledTime == null
                || !scheduledTime.isAfter(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException(
                "Scheduled time must be in the future");
        }

        this.scheduledTime = scheduledTime;
    }

    /**
     * Returns the scheduled delivery date and time.
     *
     * @return the scheduled delivery time
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }
}

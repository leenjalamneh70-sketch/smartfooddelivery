
package smart_food_delivery.domain;

/**
 * Represents a motorcycle delivery driver in the
 * Smart Food Delivery Management System.
 *
 * A motorcycle driver can deliver orders when
 * the delivery distance does not exceed 15 km
 * and the total order size does not exceed 8 items.
 *
 * This class extends Driver and overrides the
 * canDeliver method to apply motorcycle-specific rules.
 */
public class MotorcycleDriver extends Driver {

    private static final double MAX_DISTANCE = 15.0;
    private static final int MAX_ORDER_SIZE = 8;

    /**
     * Creates a new motorcycle delivery driver.
     *
     * @param name the driver's name
     * @param available whether the driver is available
     * @param distance the driver's distance used for selection
     * @param estimatedMinutes the estimated delivery time in minutes
     */
    public MotorcycleDriver(String name, boolean available,
                            double distance, int estimatedMinutes) {

        super(name, available, distance, estimatedMinutes);
    }

    /**
     * Checks whether the motorcycle driver can deliver an order.
     *
     * The driver must be available, the delivery distance
     * must not exceed 15 km, and the order size must
     * not exceed 8 items.
     *
     * @param distanceKm the delivery distance in kilometers
     * @param orderSize the total number of items in the order
     * @return true if the motorcycle driver can deliver
     *         the order, otherwise false
     */
    @Override
    public boolean canDeliver(double distanceKm, int orderSize) {

        return super.canDeliver(distanceKm, orderSize)
                && distanceKm <= MAX_DISTANCE
                && orderSize <= MAX_ORDER_SIZE;
    }
}

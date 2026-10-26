
package smart_food_delivery.domain;

/**
 * Represents a bicycle delivery driver in the
 * Smart Food Delivery Management System.
 *
 * A bicycle driver can deliver orders only when
 * the delivery distance does not exceed 5 km
 * and the total order size does not exceed 3 items.
 *
 * This class extends Driver and overrides the
 * canDeliver method to apply bicycle-specific rules.
 */
public class BicycleDriver extends Driver {

    private static final double MAX_DISTANCE = 5.0;
    private static final int MAX_ORDER_SIZE = 3;

    /**
     * Creates a new bicycle driver.
     *
     * @param name the driver's name
     * @param available whether the driver is available
     * @param distance the driver's distance used for selection
     * @param estimatedMinutes the estimated delivery time in minutes
     */
    public BicycleDriver(String name, boolean available,
                         double distance, int estimatedMinutes) {

        super(name, available, distance, estimatedMinutes);
    }

    /**
     * Checks whether the bicycle driver can deliver an order.
     *
     * The driver must be available, the delivery distance
     * must not exceed 5 km, and the order size must
     * not exceed 3 items.
     *
     * @param distanceKm the delivery distance in kilometers
     * @param orderSize the total number of items in the order
     * @return true if the bicycle driver can deliver
     *         the order, otherwise false
     */
    @Override
    public boolean canDeliver(double distanceKm, int orderSize) {

        return super.canDeliver(distanceKm, orderSize)
                && distanceKm <= MAX_DISTANCE
                && orderSize <= MAX_ORDER_SIZE;
    }
}

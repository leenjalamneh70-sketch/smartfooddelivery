
package smart_food_delivery.domain;

/**
 * Represents an electric vehicle delivery driver in the
 * Smart Food Delivery Management System.
 *
 * An electric vehicle driver can deliver an order only
 * when the remaining battery range is sufficient
 * for the delivery distance.
 *
 * The total order size must not exceed 30 items.
 *
 * This class extends Driver and overrides the
 * canDeliver method to apply electric vehicle rules.
 */
public class ElectricVehicleDriver extends Driver {

    private final double remainingRangeKm;
    private static final int MAX_ORDER_SIZE = 30;

    /**
     * Creates a new electric vehicle delivery driver.
     *
     * @param name the driver's name
     * @param available whether the driver is available
     * @param distance the driver's distance used for selection
     * @param estimatedMinutes the estimated delivery time in minutes
     * @param remainingRangeKm the remaining battery range in km
     * @throws IllegalArgumentException if the battery range
     *         is negative, infinite, or NaN
     */
    public ElectricVehicleDriver(String name, boolean available,
                                 double distance, int estimatedMinutes,
                                 double remainingRangeKm) {

        super(name, available, distance, estimatedMinutes);

        if (!Double.isFinite(remainingRangeKm)
                || remainingRangeKm < 0) {
            throw new IllegalArgumentException("Invalid battery range");
        }

        this.remainingRangeKm = remainingRangeKm;
    }

    /**
     * Returns the remaining battery range of the vehicle.
     *
     * @return the remaining battery range in kilometers
     */
    public double getRemainingRangeKm() {
        return remainingRangeKm;
    }

    /**
     * Checks whether the electric vehicle driver
     * can deliver a specific order.
     *
     * The driver must be available, the delivery distance
     * must not exceed the remaining battery range,
     * and the order size must not exceed 30 items.
     *
     * @param distanceKm the delivery distance in kilometers
     * @param orderSize the total number of items in the order
     * @return true if the driver can deliver the order,
     *         otherwise false
     */
    @Override
    public boolean canDeliver(double distanceKm, int orderSize) {

        return super.canDeliver(distanceKm, orderSize)
                && distanceKm <= remainingRangeKm
                && orderSize <= MAX_ORDER_SIZE;
    }
}

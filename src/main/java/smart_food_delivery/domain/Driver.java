
package smart_food_delivery.domain;

/**
 * Represents a delivery driver in the
 * Smart Food Delivery Management System.
 *
 * This class stores the driver's information,
 * availability, distance, and estimated delivery time.
 *
 * It also provides a general delivery eligibility
 * method that can be overridden by specific driver types.
 */
public class Driver {

    private String name;
    private boolean available;
    private double distance;
    private int estimatedMinutes;

    /**
     * Creates a new delivery driver.
     *
     * @param name the driver's name
     * @param available whether the driver is available
     * @param distance the driver's distance used for selection
     * @param estimatedMinutes the estimated delivery time in minutes
     */
    public Driver(String name, boolean available,
                  double distance, int estimatedMinutes) {

        this.name = name;
        this.available = available;
        this.distance = distance;
        this.estimatedMinutes = estimatedMinutes;
    }

    /**
     * Returns the driver's name.
     *
     * @return the driver's name
     */
    public String getName() {
        return name;
    }

    /**
     * Checks whether the driver is available.
     *
     * @return true if the driver is available,
     *         otherwise false
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * Updates the driver's availability.
     *
     * @param available the new availability status
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * Returns the driver's distance used for selection.
     *
     * @return the driver's distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Returns the estimated delivery time.
     *
     * @return the estimated delivery time in minutes
     */
    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    /**
     * Checks whether the driver is eligible
     * to deliver a specific order.
     *
     * The driver must be available, the distance
     * must be finite and non-negative, and the
     * order must contain at least one item.
     *
     * Subclasses can override this method to
     * apply their own delivery restrictions.
     *
     * @param distanceKm the delivery distance in kilometers
     * @param orderSize the total number of items in the order
     * @return true if the driver can deliver the order,
     *         otherwise false
     */
    public boolean canDeliver(double distanceKm, int orderSize) {

        return available
                && Double.isFinite(distanceKm)
                && distanceKm >= 0
                && orderSize > 0;
    }
}


package smart_food_delivery.service;

/**
 * Calculates the delivery cost for an order
 * in the Smart Food Delivery Management System.
 *
 * The delivery cost is calculated based on
 * the delivery distance and a fixed cost per kilometer.
 */
public class DeliveryCostService {

    private static final double COST_PER_KM = 2.0;

    /**
     * Calculates the delivery cost based on distance.
     *
     * The cost is calculated by multiplying the
     * delivery distance by the cost per kilometer.
     *
     * @param distance the delivery distance in kilometers
     * @return the calculated delivery cost
     * @throws IllegalArgumentException if the distance
     *         is negative, infinite, or NaN
     */
    public double calculateDeliveryCost(double distance) {

        if (Double.isNaN(distance) || Double.isInfinite(distance)
                || distance < 0) {
            throw new IllegalArgumentException(
                "Invalid delivery distance");
        }

        return distance * COST_PER_KM;
    }
}


package smart_food_delivery.service;

/**
 * Defines the pricing strategy interface
 * in the Smart Food Delivery Management System.
 *
 * This interface provides a common method for
 * calculating order prices using different
 * pricing strategies.
 *
 * Implementations may apply normal pricing,
 * express fees, peak-hour increases, or discounts.
 */
public interface PricingStrategy {

    /**
     * Calculates the order price according
     * to the selected pricing strategy.
     *
     * @param orderTotal the original order total
     * @return the calculated price after applying
     *         the pricing strategy
     */
    double calculatePrice(double orderTotal);

}

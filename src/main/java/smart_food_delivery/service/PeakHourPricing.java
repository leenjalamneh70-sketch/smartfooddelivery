
package smart_food_delivery.service;

/**
 * Implements the peak-hour pricing strategy
 * in the Smart Food Delivery Management System.
 *
 * This strategy increases the original order
 * total by 20 percent during peak hours.
 *
 * Implements the PricingStrategy interface.
 */
public class PeakHourPricing implements PricingStrategy {

    /**
     * Calculates the order price by applying
     * a 20 percent peak-hour increase.
     *
     * @param orderTotal the original order total
     * @return the order total after a 20 percent increase
     */
    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal * 1.20;
    }
}

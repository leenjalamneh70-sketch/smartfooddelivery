
package smart_food_delivery.service;

/**
 * Implements the normal pricing strategy
 * in the Smart Food Delivery Management System.
 *
 * This strategy returns the original order total
 * without applying any additional charges
 * or discounts.
 *
 * Implements the PricingStrategy interface.
 */
public class NormalPricing implements PricingStrategy {

    /**
     * Calculates the normal price of an order.
     *
     * Returns the original order total without
     * applying any pricing adjustments.
     *
     * @param orderTotal the original order total
     * @return the original order total
     */
    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal;
    }
}


package smart_food_delivery.service;

/**
 * Implements the promotional pricing strategy
 * in the Smart Food Delivery Management System.
 *
 * This strategy applies a 10 percent discount
 * to the original order total.
 *
 * Implements the PricingStrategy interface.
 */
public class PromotionalPricing implements PricingStrategy {

    /**
     * Calculates the order price after applying
     * a 10 percent promotional discount.
     *
     * @param orderTotal the original order total
     * @return the order total after a 10 percent discount
     */
    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal * 0.90;
    }
}


package smart_food_delivery.service;

/**
 * Implements the express pricing strategy
 * in the Smart Food Delivery Management System.
 *
 * This strategy adds a fixed express fee of 10
 * to the original order total.
 *
 * Implements the PricingStrategy interface.
 */
public class ExpressPricing implements PricingStrategy {

    /**
     * Calculates the price of an express order
     * by adding a fixed fee of 10.
     *
     * @param orderTotal the original order total
     * @return the order total including the express fee
     */
    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal + 10;
    }
}

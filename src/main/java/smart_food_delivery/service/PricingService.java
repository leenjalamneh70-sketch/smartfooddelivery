
package smart_food_delivery.service;

import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.ExpressOrder;
import smart_food_delivery.domain.OrderStatus;
import smart_food_delivery.domain.PaymentStatus;

/**
 * Handles order pricing and final bill calculation
 * in the Smart Food Delivery Management System.
 *
 * This service applies a pricing strategy, calculates
 * delivery costs, and handles additional express fees.
 *
 * It also prevents changes to orders that have already
 * been delivered, cancelled, or paid.
 */
public class PricingService {

    private DeliveryCostService deliveryCostService;

    /**
     * Creates a pricing service with a delivery
     * cost calculation service.
     */
    public PricingService() {
        this.deliveryCostService = new DeliveryCostService();
    }

    /**
     * Calculates the final bill for an order.
     *
     * The calculation includes the food subtotal,
     * pricing strategy adjustments, delivery costs,
     * and any applicable express delivery fee.
     *
     * ExpressPricing adds a fixed fee of 10 to the
     * food subtotal. When it is used with ExpressOrder,
     * the express fee must also equal 10 to avoid
     * charging the customer twice.
     *
     * The calculated delivery distance, delivery fee,
     * and final bill are saved in the order.
     *
     * @param order the order to calculate the bill for
     * @param strategy the pricing strategy to apply
     * @param distance the delivery distance in kilometers
     * @return the final bill amount
     * @throws IllegalArgumentException if the order,
     *         pricing strategy, express fee, or calculated
     *         amount is invalid
     * @throws IllegalStateException if the order has been
     *         delivered, cancelled, or already paid
     */
    public double calculateFinalBill(Order order,
                                     PricingStrategy strategy,
                                     double distance) {

        if (order == null || strategy == null) {
            throw new IllegalArgumentException(
                "Invalid order or strategy");
        }

        if (strategy instanceof ExpressPricing
                && !(order instanceof ExpressOrder)) {
            throw new IllegalArgumentException(
                "Express pricing requires an express order");
        }

        if (order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED
                || order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException(
                "Order cannot be modified");
        }

        double foodTotal = order.getTotal();

        double adjustedPrice = strategy.calculatePrice(foodTotal);

        if (!Double.isFinite(adjustedPrice)
                || adjustedPrice < 0) {
            throw new IllegalArgumentException(
                "Invalid calculated price");
        }

        double deliveryFee =
            deliveryCostService.calculateDeliveryCost(distance);

        // Add express fee only if it was not already
        // included by the ExpressPricing strategy.
        if (order instanceof ExpressOrder) {

            ExpressOrder expressOrder = (ExpressOrder) order;

            if (strategy instanceof ExpressPricing) {

                if (Double.compare(
                        expressOrder.getExpressFee(), 10.0) != 0) {
                    throw new IllegalArgumentException(
                        "Express fee must match ExpressPricing fee");
                }

            } else {
                deliveryFee += expressOrder.getExpressFee();
            }
        }

        double finalPrice = adjustedPrice + deliveryFee;

        if (!Double.isFinite(finalPrice) || finalPrice < 0) {
            throw new IllegalArgumentException(
                "Invalid final price");
        }

        order.setDeliveryDistance(distance);
        order.setDeliveryFee(deliveryFee);
        order.setFinalBill(finalPrice);

        return finalPrice;
    }
}

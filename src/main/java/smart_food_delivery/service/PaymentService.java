
package smart_food_delivery.service;

import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.OrderStatus;
import smart_food_delivery.domain.PaymentStatus;

/**
 * Handles cash payments for orders in the
 * Smart Food Delivery Management System.
 *
 * Validates the order status and payment amount
 * before marking an order as paid.
 */
public class PaymentService {

    /**
     * Processes a cash payment for a delivered order.
     *
     * The payment is accepted only if the order exists,
     * has not already been paid, has been delivered,
     * and the provided amount matches the final bill
     * within a tolerance of 0.001.
     *
     * @param order the order to be paid
     * @param amount the cash amount provided by the customer
     * @return true if the payment is successful,
     *         otherwise false
     */
    public boolean processCashPayment(Order order, double amount) {

        if (order == null) {
            return false;
        }

        if (order.getPaymentStatus() == PaymentStatus.PAID) {
            return false;
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            return false;
        }

        if (Double.isNaN(amount) || Double.isInfinite(amount)
                || amount < 0) {
            return false;
        }

        if (Math.abs(amount - order.getFinalBill()) > 0.001) {
            return false;
        }

        order.markAsPaid("Cash");

        return true;
    }
}

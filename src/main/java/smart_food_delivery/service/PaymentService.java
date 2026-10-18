package smart_food_delivery.service;

import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.OrderStatus;
import smart_food_delivery.domain.PaymentStatus;

public class PaymentService {

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

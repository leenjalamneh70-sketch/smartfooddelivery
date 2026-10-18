package smart_food_delivery.service;

import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.OrderStatus;
import smart_food_delivery.domain.PaymentStatus;

public class PricingService {

    private DeliveryCostService deliveryCostService;

    public PricingService() {
        this.deliveryCostService = new DeliveryCostService();
    }

    public double calculateFinalBill(Order order,
                                     PricingStrategy strategy,
                                     double distance) {

        if (order == null || strategy == null) {
            throw new IllegalArgumentException("Invalid order or strategy");
        }

        if (order.getStatus() == OrderStatus.DELIVERED
                || order.getStatus() == OrderStatus.CANCELLED
                || order.getPaymentStatus() == PaymentStatus.PAID) {
            throw new IllegalStateException("Order cannot be modified");
        }

        double foodTotal = order.getTotal();

        double adjustedPrice = strategy.calculatePrice(foodTotal);

        if (Double.isNaN(adjustedPrice)
                || Double.isInfinite(adjustedPrice)
                || adjustedPrice < 0) {
            throw new IllegalArgumentException("Invalid calculated price");
        }

        double deliveryFee =
            deliveryCostService.calculateDeliveryCost(distance);

        double finalPrice = adjustedPrice + deliveryFee;

        order.setDeliveryDistance(distance);
        order.setDeliveryFee(deliveryFee);
        order.setFinalBill(finalPrice);

        return finalPrice;
    }
}

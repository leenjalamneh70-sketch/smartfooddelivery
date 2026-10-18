package smart_food_delivery.service;

public class PromotionalPricing implements PricingStrategy {

    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal * 0.90;
    }
}

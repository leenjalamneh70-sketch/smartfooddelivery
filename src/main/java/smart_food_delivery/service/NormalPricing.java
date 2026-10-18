package smart_food_delivery.service;

public class NormalPricing implements PricingStrategy {

    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal;
    }
}

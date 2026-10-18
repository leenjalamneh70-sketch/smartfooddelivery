package smart_food_delivery.service;

public class PeakHourPricing implements PricingStrategy {

    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal * 1.20;
    }
}

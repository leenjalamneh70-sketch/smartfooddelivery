package smart_food_delivery.service;

public class ExpressPricing implements PricingStrategy {

    @Override
    public double calculatePrice(double orderTotal) {
        return orderTotal + 10;
    }
}

package smart_food_delivery.service;

public interface PricingStrategy {

    double calculatePrice(double orderTotal);

}

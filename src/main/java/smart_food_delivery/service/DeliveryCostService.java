package smart_food_delivery.service;

public class DeliveryCostService {

    private static final double COST_PER_KM = 2.0;

    public double calculateDeliveryCost(double distance) {

        if (Double.isNaN(distance) || Double.isInfinite(distance)
                || distance < 0) {
            throw new IllegalArgumentException("Invalid delivery distance");
        }

        return distance * COST_PER_KM;
    }
}

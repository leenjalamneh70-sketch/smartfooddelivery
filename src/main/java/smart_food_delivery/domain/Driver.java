
package smart_food_delivery.domain;

public class Driver {

    private String name;
    private boolean available;
    private double distance;
    private int estimatedMinutes;

    public Driver(String name, boolean available,
                  double distance, int estimatedMinutes) {

        this.name = name;
        this.available = available;
        this.distance = distance;
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getDistance() {
        return distance;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }
}

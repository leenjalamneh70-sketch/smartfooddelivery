
package smart_food_delivery.domain;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private String name;
    private boolean open;
    private double minimumOrder;
    private String deliveryZone;
    private List<MenuItem> menu;

    public Restaurant(String name, boolean open,
                      double minimumOrder, String deliveryZone) {
        this.name = name;
        this.open = open;
        this.minimumOrder = minimumOrder;
        this.deliveryZone = deliveryZone;
        this.menu = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public boolean isOpen() {
        return open;
    }

    public double getMinimumOrder() {
        return minimumOrder;
    }

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void addMenuItem(MenuItem item) {
        if (item != null) {
            menu.add(item);
        }
    }

    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }
}

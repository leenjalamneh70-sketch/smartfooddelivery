
package smart_food_delivery.domain;

public class OrderItem {

    private MenuItem item;
    private int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return item.getPrice() * quantity;
    }
}

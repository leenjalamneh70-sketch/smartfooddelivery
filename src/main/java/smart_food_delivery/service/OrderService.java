
package smart_food_delivery.service;

import java.util.ArrayList;
import java.util.List;

import smart_food_delivery.domain.*;

public class OrderService {

    private List<Order> orders = new ArrayList<>();
    private int nextId = 1;

    public Order placeOrder(Restaurant restaurant,
                            List<OrderItem> items,
                            String address,
                            String zone) {

        if (restaurant == null || !restaurant.isOpen()) {
            throw new IllegalArgumentException("Restaurant is closed or invalid");
        }

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order cannot be empty");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid delivery address");
        }

        if (zone == null ||
            !restaurant.getDeliveryZone().equalsIgnoreCase(zone.trim())) {
            throw new IllegalArgumentException("Outside delivery zone");
        }

        for (OrderItem orderItem : items) {

            if (orderItem == null || orderItem.getItem() == null) {
                throw new IllegalArgumentException("Invalid item");
            }

            if (orderItem.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity");
            }

            if (!orderItem.getItem().isAvailable()) {
                throw new IllegalArgumentException("Unavailable item");
            }

            if (!restaurant.getMenu().contains(orderItem.getItem())) {
                throw new IllegalArgumentException(
                    "Item does not belong to restaurant");
            }

            if (orderItem.getItem().getPrice() < 0) {
                throw new IllegalArgumentException("Invalid item price");
            }
        }

        Order order = new Order(nextId, restaurant, items, address, zone);

        if (order.getTotal() < restaurant.getMinimumOrder()) {
            throw new IllegalArgumentException(
                "Order is below minimum order value");
        }

        orders.add(order);
        nextId++;

        return order;
    }

    public boolean cancelOrder(Order order) {

        if (order == null || !orders.contains(order)) {
            return false;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            return false;
        }

        order.setStatus(OrderStatus.CANCELLED);
        return true;
    }

    public List<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}

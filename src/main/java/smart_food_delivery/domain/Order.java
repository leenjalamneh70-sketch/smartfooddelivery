
package smart_food_delivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer food order.
 * Stores ordered items, delivery information, order status,
 * pricing details, and payment status.
 */
public class Order {

    private int id;
    private Restaurant restaurant;
    private List<OrderItem> items;
    private String deliveryAddress;
    private String deliveryZone;

    private OrderStatus status;
    private Driver assignedDriver;

    private double deliveryDistance;
    private double deliveryFee;
    private double finalBill;

    private PaymentStatus paymentStatus;
    private String paymentMethod;

    /**
     * Creates a new pending order.
     *
     * @param id unique order identifier
     * @param restaurant selected restaurant
     * @param items ordered menu items
     * @param deliveryAddress customer delivery address
     * @param deliveryZone delivery area
     */
    public Order(int id, Restaurant restaurant,
                 List<OrderItem> items,
                 String deliveryAddress, String deliveryZone) {

        if (restaurant == null || items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Invalid order data");
        }

        this.id = id;
        this.restaurant = restaurant;
        this.items = new ArrayList<>(items);
        this.deliveryAddress = deliveryAddress;
        this.deliveryZone = deliveryZone;

        this.status = OrderStatus.PENDING;
        this.paymentStatus = PaymentStatus.UNPAID;
        this.paymentMethod = null;

        this.deliveryDistance = 0;
        this.deliveryFee = 0;
        this.finalBill = getTotal();
    }

    public int getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    public void setAssignedDriver(Driver driver) {
        this.assignedDriver = driver;
    }

    /**
     * Calculates the subtotal of all ordered items.
     *
     * @return subtotal before pricing adjustments and delivery fees
     */
    public double getTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(double distance) {
        if (Double.isNaN(distance) || Double.isInfinite(distance)
                || distance < 0) {
            throw new IllegalArgumentException("Invalid distance");
        }

        this.deliveryDistance = distance;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        if (Double.isNaN(deliveryFee) || Double.isInfinite(deliveryFee)
                || deliveryFee < 0) {
            throw new IllegalArgumentException("Invalid delivery fee");
        }

        this.deliveryFee = deliveryFee;
    }

    public double getFinalBill() {
        return finalBill;
    }

    public void setFinalBill(double finalBill) {
        if (Double.isNaN(finalBill) || Double.isInfinite(finalBill)
                || finalBill < 0) {
            throw new IllegalArgumentException("Invalid final bill");
        }

        this.finalBill = finalBill;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void markAsPaid(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid payment method");
        }

        this.paymentMethod = method;
        this.paymentStatus = PaymentStatus.PAID;
    }
}

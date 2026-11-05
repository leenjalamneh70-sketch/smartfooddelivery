
package smart_food_delivery.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer food order in the
 * Smart Food Delivery Management System.
 *
 * Stores ordered items, delivery information,
 * order status, assigned driver, pricing details,
 * and payment status.
 *
 * This class is the parent class for StandardOrder,
 * ExpressOrder, and ScheduledOrder.
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
     * @throws IllegalArgumentException if restaurant
     *         is null or items are null or empty
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

    /**
     * Prevents changes to delivery information
     * after the order has been delivered.
     *
     * Payment recording is handled separately
     * and remains possible after delivery.
     */
    private void ensureModifiable() {
        if (status == OrderStatus.DELIVERED) {
            throw new IllegalStateException(
                "Delivered orders cannot be modified");
        }
    }

    /**
     * Returns the unique order ID.
     *
     * @return the order ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the restaurant associated with the order.
     *
     * @return the restaurant
     */
    public Restaurant getRestaurant() {
        return restaurant;
    }

    /**
     * Returns a copy of the ordered items list.
     *
     * @return a new list containing the order items
     */
    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Returns the customer delivery address.
     *
     * @return the delivery address
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Returns the delivery zone.
     *
     * @return the delivery zone
     */
    public String getDeliveryZone() {
        return deliveryZone;
    }

    /**
     * Returns the current order status.
     *
     * @return the order status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Updates the order status.
     *
     * Delivered orders cannot change status.
     * Lifecycle validation is handled by DeliveryService.
     *
     * @param status the new order status
     * @throws IllegalArgumentException if status is null
     * @throws IllegalStateException if already delivered
     */
    public void setStatus(OrderStatus status) {

        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        ensureModifiable();
        this.status = status;
    }

    /**
     * Returns the assigned driver.
     *
     * @return assigned driver, or null
     */
    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    /**
     * Assigns a driver to the order.
     *
     * @param driver the driver to assign
     */
    public void setAssignedDriver(Driver driver) {
        ensureModifiable();
        this.assignedDriver = driver;
    }

    /**
     * Calculates the subtotal of ordered items.
     *
     * @return subtotal before delivery fees
     */
    public double getTotal() {

        double total = 0;

        for (OrderItem item : items) {
            total += item.getSubtotal();
        }

        return total;
    }

    /**
     * Returns the delivery distance.
     *
     * @return distance in kilometers
     */
    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    /**
     * Updates the delivery distance.
     *
     * @param distance delivery distance
     * @throws IllegalArgumentException if invalid
     */
    public void setDeliveryDistance(double distance) {

        ensureModifiable();

        if (Double.isNaN(distance) || Double.isInfinite(distance)
                || distance < 0) {
            throw new IllegalArgumentException("Invalid distance");
        }

        this.deliveryDistance = distance;
    }

    /**
     * Returns the delivery fee.
     *
     * @return delivery fee
     */
    public double getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * Updates the delivery fee.
     *
     * @param deliveryFee new delivery fee
     * @throws IllegalArgumentException if invalid
     */
    public void setDeliveryFee(double deliveryFee) {

        ensureModifiable();

        if (Double.isNaN(deliveryFee)
                || Double.isInfinite(deliveryFee)
                || deliveryFee < 0) {
            throw new IllegalArgumentException("Invalid delivery fee");
        }

        this.deliveryFee = deliveryFee;
    }

    /**
     * Returns the final bill amount.
     *
     * @return final bill
     */
    public double getFinalBill() {
        return finalBill;
    }

    /**
     * Updates the final bill amount.
     *
     * @param finalBill new final bill
     * @throws IllegalArgumentException if invalid
     */
    public void setFinalBill(double finalBill) {

        ensureModifiable();

        if (Double.isNaN(finalBill)
                || Double.isInfinite(finalBill)
                || finalBill < 0) {
            throw new IllegalArgumentException("Invalid final bill");
        }

        this.finalBill = finalBill;
    }

    /**
     * Returns the current payment status.
     *
     * @return payment status
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Returns the payment method.
     *
     * @return payment method, or null
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Marks the order as paid.
     *
     * Cash payment may be recorded after delivery.
     * PaymentService validates the payment before
     * calling this method.
     *
     * @param method the payment method
     * @throws IllegalArgumentException if method is invalid
     */
    public void markAsPaid(String method) {

        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid payment method");
        }

        this.paymentMethod = method;
        this.paymentStatus = PaymentStatus.PAID;
    }
}

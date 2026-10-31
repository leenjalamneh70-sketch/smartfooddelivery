
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
     * @throws IllegalArgumentException if the restaurant
     *         is null or the items list is null or empty
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
     * @param status the new order status
     * @throws IllegalArgumentException if status is null
     */
    public void setStatus(OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    /**
     * Returns the driver assigned to the order.
     *
     * @return the assigned driver, or null if none
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
        this.assignedDriver = driver;
    }

    /**
     * Calculates the subtotal of all ordered items.
     *
     * @return subtotal before pricing adjustments
     *         and delivery fees
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
     * @return the delivery distance in kilometers
     */
    public double getDeliveryDistance() {
        return deliveryDistance;
    }

    /**
     * Updates the delivery distance.
     *
     * @param distance the delivery distance in kilometers
     * @throws IllegalArgumentException if the distance
     *         is negative, infinite, or NaN
     */
    public void setDeliveryDistance(double distance) {
        if (Double.isNaN(distance) || Double.isInfinite(distance)
                || distance < 0) {
            throw new IllegalArgumentException("Invalid distance");
        }

        this.deliveryDistance = distance;
    }

    /**
     * Returns the delivery fee.
     *
     * @return the delivery fee
     */
    public double getDeliveryFee() {
        return deliveryFee;
    }

    /**
     * Updates the delivery fee.
     *
     * @param deliveryFee the new delivery fee
     * @throws IllegalArgumentException if the fee
     *         is negative, infinite, or NaN
     */
    public void setDeliveryFee(double deliveryFee) {
        if (Double.isNaN(deliveryFee) || Double.isInfinite(deliveryFee)
                || deliveryFee < 0) {
            throw new IllegalArgumentException("Invalid delivery fee");
        }

        this.deliveryFee = deliveryFee;
    }

    /**
     * Returns the final bill amount.
     *
     * @return the final bill
     */
    public double getFinalBill() {
        return finalBill;
    }

    /**
     * Updates the final bill amount.
     *
     * @param finalBill the new final bill
     * @throws IllegalArgumentException if the amount
     *         is negative, infinite, or NaN
     */
    public void setFinalBill(double finalBill) {
        if (Double.isNaN(finalBill) || Double.isInfinite(finalBill)
                || finalBill < 0) {
            throw new IllegalArgumentException("Invalid final bill");
        }

        this.finalBill = finalBill;
    }

    /**
     * Returns the current payment status.
     *
     * @return the payment status
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Returns the payment method used for the order.
     *
     * @return the payment method, or null if not set
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Marks the order as paid using the specified
     * payment method.
     *
     * @param method the payment method
     * @throws IllegalArgumentException if the payment
     *         method is null or blank
     */
    public void markAsPaid(String method) {
        if (method == null || method.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid payment method");
        }

        this.paymentMethod = method;
        this.paymentStatus = PaymentStatus.PAID;
    }
}

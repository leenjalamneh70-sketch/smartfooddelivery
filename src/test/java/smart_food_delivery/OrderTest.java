
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;

class OrderTest {

    private Restaurant restaurant;
    private MenuItem pizza;
    private OrderItem item;
    private Order order;

    @BeforeEach
    void setup() {

        restaurant = new Restaurant(
            "Pizza House", true, 20, "City"
        );

        pizza = new MenuItem("Pizza", 50, true);
        restaurant.addMenuItem(pizza);

        item = new OrderItem(pizza, 2);

        order = new Order(
            1,
            restaurant,
            Arrays.asList(item),
            "Main Street",
            "City"
        );
    }

    @Test
    void testValidOrderCreation() {

        assertEquals(1, order.getId());
        assertEquals(restaurant, order.getRestaurant());
        assertEquals("Main Street", order.getDeliveryAddress());
        assertEquals("City", order.getDeliveryZone());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(PaymentStatus.UNPAID, order.getPaymentStatus());
        assertNull(order.getPaymentMethod());
        assertNull(order.getAssignedDriver());
        assertEquals(100.0, order.getTotal(), 0.001);
        assertEquals(100.0, order.getFinalBill(), 0.001);
        assertEquals(0.0, order.getDeliveryDistance(), 0.001);
        assertEquals(0.0, order.getDeliveryFee(), 0.001);
    }

    @Test
    void testNullRestaurantRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            new Order(
                1, null, Arrays.asList(item),
                "Main Street", "City"
            )
        );
    }

    @Test
    void testNullItemsRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            new Order(
                1, restaurant, null,
                "Main Street", "City"
            )
        );
    }

    @Test
    void testEmptyItemsRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            new Order(
                1, restaurant, Collections.emptyList(),
                "Main Street", "City"
            )
        );
    }

    @Test
    void testSetValidStatus() {

        order.setStatus(OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void testNullStatusRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setStatus(null)
        );
    }

    @Test
    void testValidDeliveryDistance() {

        order.setDeliveryDistance(5.0);

        assertEquals(5.0, order.getDeliveryDistance(), 0.001);
    }

    @Test
    void testNegativeDeliveryDistanceRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryDistance(-1.0)
        );
    }

    @Test
    void testNaNDeliveryDistanceRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryDistance(Double.NaN)
        );
    }

    @Test
    void testInfiniteDeliveryDistanceRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryDistance(Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void testValidDeliveryFee() {

        order.setDeliveryFee(10.0);

        assertEquals(10.0, order.getDeliveryFee(), 0.001);
    }

    @Test
    void testNegativeDeliveryFeeRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryFee(-5.0)
        );
    }

    @Test
    void testNaNDeliveryFeeRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryFee(Double.NaN)
        );
    }

    @Test
    void testInfiniteDeliveryFeeRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setDeliveryFee(Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void testValidFinalBill() {

        order.setFinalBill(150.0);

        assertEquals(150.0, order.getFinalBill(), 0.001);
    }

    @Test
    void testNegativeFinalBillRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setFinalBill(-10.0)
        );
    }

    @Test
    void testNaNFinalBillRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setFinalBill(Double.NaN)
        );
    }

    @Test
    void testInfiniteFinalBillRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.setFinalBill(Double.POSITIVE_INFINITY)
        );
    }

    @Test
    void testValidPayment() {

        order.markAsPaid("CASH");

        assertEquals(PaymentStatus.PAID, order.getPaymentStatus());
        assertEquals("CASH", order.getPaymentMethod());
    }

    @Test
    void testNullPaymentMethodRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.markAsPaid(null)
        );
    }

    @Test
    void testEmptyPaymentMethodRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.markAsPaid("")
        );
    }

    @Test
    void testBlankPaymentMethodRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            order.markAsPaid("   ")
        );
    }

    @Test
    void testItemsListIsCopied() {

        assertEquals(1, order.getItems().size());

        order.getItems().clear();

        assertEquals(1, order.getItems().size());
    }
}

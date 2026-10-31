
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.OrderService;

class OrderServiceTest {

    private Restaurant restaurant;
    private MenuItem pizza;
    private OrderService service;

    @BeforeEach
    void setup() {

        restaurant = new Restaurant("Pizza House", true, 20, "City");
        pizza = new MenuItem("Pizza", 30, true);

        restaurant.addMenuItem(pizza);

        service = new OrderService();
    }

    @Test
    void testPlaceOrder() {

        Order order = service.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City"
        );

        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(60, order.getTotal());
        assertEquals(1, service.getOrders().size());
    }

    @Test
    void testEmptyOrderRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                restaurant,
                Collections.emptyList(),
                "Main Street",
                "City"
            )
        );
    }

    @Test
    void testInvalidQuantityRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                restaurant,
                Arrays.asList(new OrderItem(pizza, 0)),
                "Main Street",
                "City"
            )
        );
    }

    @Test
    void testUnavailableItemRejected() {

        MenuItem unavailable = new MenuItem("Burger", 25, false);
        restaurant.addMenuItem(unavailable);

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                restaurant,
                Arrays.asList(new OrderItem(unavailable, 1)),
                "Main Street",
                "City"
            )
        );
    }

    @Test
    void testOutsideDeliveryZoneRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                restaurant,
                Arrays.asList(new OrderItem(pizza, 1)),
                "Main Street",
                "Outside City"
            )
        );
    }

    @Test
    void testInvalidAddressRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                restaurant,
                Arrays.asList(new OrderItem(pizza, 1)),
                "",
                "City"
            )
        );
    }

    @Test
    void testBelowMinimumOrderRejected() {

        Restaurant expensive =
            new Restaurant("Expensive", true, 100, "City");

        expensive.addMenuItem(pizza);

        assertThrows(IllegalArgumentException.class, () ->
            service.placeOrder(
                expensive,
                Arrays.asList(new OrderItem(pizza, 1)),
                "Main Street",
                "City"
            )
        );
    }

    @Test
    void testPendingOrderCanBeCancelled() {

        Order order = service.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 1)),
            "Main Street",
            "City"
        );

        assertTrue(service.cancelOrder(order));
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test
    void testPreparingOrderCannotBeCancelled() {

        Order order = service.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 1)),
            "Main Street",
            "City"
        );

        order.setStatus(OrderStatus.PREPARING);

        assertFalse(service.cancelOrder(order));
        assertEquals(OrderStatus.PREPARING, order.getStatus());
    }
}

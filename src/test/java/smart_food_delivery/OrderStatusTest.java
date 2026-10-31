package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class OrderStatusTest {

    private Order order;
    private DeliveryService service;
    private NotificationService notifications;

    @BeforeEach
    void setup() {

        Restaurant restaurant =
            new Restaurant("Pizza House", true, 20, "City");

        MenuItem pizza = new MenuItem("Pizza", 30, true);
        restaurant.addMenuItem(pizza);

        OrderService orderService = new OrderService();

        order = orderService.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 1)),
            "Main Street",
            "City"
        );

        notifications = mock(NotificationService.class);

        service = new DeliveryService(
            new NearestDriverStrategy(),
            notifications
        );
    }

    @Test
    void testValidOrderLifecycle() {

        Driver driver = new Driver("Ali", true, 3, 10);

        assertTrue(service.assignDriver(
            order, Arrays.asList(driver)));

        service.updateOrderStatus(order, OrderStatus.CONFIRMED);
        service.updateOrderStatus(order, OrderStatus.PREPARING);
        service.updateOrderStatus(order, OrderStatus.READY);
        service.updateOrderStatus(order, OrderStatus.OUT_FOR_DELIVERY);
        service.updateOrderStatus(order, OrderStatus.DELIVERED);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertTrue(driver.isAvailable());

        verify(notifications, times(6)).sendNotification(anyString());
    }

    @Test
    void testInvalidTransitionRejected() {

        assertThrows(IllegalStateException.class, () ->
            service.updateOrderStatus(
                order, OrderStatus.OUT_FOR_DELIVERY)
        );

        assertEquals(OrderStatus.PENDING, order.getStatus());
    }

    @Test
    void testDeliveredOrderCannotChangeStatus() {

        order.setStatus(OrderStatus.DELIVERED);

        assertThrows(IllegalStateException.class, () ->
            service.updateOrderStatus(
                order, OrderStatus.CONFIRMED)
        );
    }

    @Test
    void testOutForDeliveryRequiresDriver() {

        service.updateOrderStatus(order, OrderStatus.CONFIRMED);
        service.updateOrderStatus(order, OrderStatus.PREPARING);
        service.updateOrderStatus(order, OrderStatus.READY);

        assertThrows(IllegalStateException.class, () ->
            service.updateOrderStatus(
                order, OrderStatus.OUT_FOR_DELIVERY)
        );
    }
}

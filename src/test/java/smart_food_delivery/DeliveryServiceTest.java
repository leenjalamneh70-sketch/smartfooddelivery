
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class DeliveryServiceTest {

    private Order order;
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
    }

    @Test
    void testNearestDriverAssignment() {

        Driver far = new Driver("Ali", true, 10, 12);
        Driver near = new Driver("Sara", true, 2, 20);

        DeliveryService service = new DeliveryService(
            new NearestDriverStrategy(), notifications);

        assertTrue(service.assignDriver(order, Arrays.asList(far, near)));

        assertSame(near, order.getAssignedDriver());
        assertFalse(near.isAvailable());

        verify(notifications).sendNotification(
            "Driver Sara assigned to order #" + order.getId()
        );
    }

    @Test
    void testFastestDriverSelection() {

        Driver first = new Driver("Ali", true, 2, 20);
        Driver second = new Driver("Sara", true, 5, 8);

        DeliveryService service = new DeliveryService(
            new FastestDriverStrategy(), notifications);

        assertTrue(service.assignDriver(order, Arrays.asList(first, second)));

        assertSame(second, order.getAssignedDriver());
    }

    @Test
    void testBusyDriverRejected() {

        Driver busy = new Driver("Ali", false, 1, 5);

        DeliveryService service = new DeliveryService(
            new NearestDriverStrategy(), notifications);

        assertFalse(service.assignDriver(order, Arrays.asList(busy)));
        assertNull(order.getAssignedDriver());

        verifyNoInteractions(notifications);
    }

    @Test
    void testOrderStatusNotification() {

        DeliveryService service = new DeliveryService(
            new NearestDriverStrategy(), notifications);

        service.updateOrderStatus(order, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, order.getStatus());

        verify(notifications).sendNotification(
            "Order #" + order.getId()
            + " status changed to CONFIRMED"
        );
    }

    @Test
    void testInvalidStatusTransition() {

        DeliveryService service = new DeliveryService(
            new NearestDriverStrategy(), notifications);

        assertThrows(IllegalStateException.class, () ->
            service.updateOrderStatus(order, OrderStatus.DELIVERED)
        );

        assertEquals(OrderStatus.PENDING, order.getStatus());
        verifyNoInteractions(notifications);
    }

    @Test
    void testDeliveryNotifications() {

        Driver driver = new Driver("Ali", true, 3, 10);

        DeliveryService service = new DeliveryService(
            new NearestDriverStrategy(), notifications);

        assertTrue(service.assignDriver(order, Arrays.asList(driver)));

        service.updateOrderStatus(order, OrderStatus.CONFIRMED);
        service.updateOrderStatus(order, OrderStatus.PREPARING);
        service.updateOrderStatus(order, OrderStatus.READY);
        service.updateOrderStatus(order, OrderStatus.OUT_FOR_DELIVERY);
        service.updateOrderStatus(order, OrderStatus.DELIVERED);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertTrue(driver.isAvailable());

        verify(notifications).sendNotification(
            "Order #" + order.getId()
            + " status changed to OUT_FOR_DELIVERY"
        );

        verify(notifications).sendNotification(
            "Order #" + order.getId()
            + " status changed to DELIVERED"
        );
    }
}

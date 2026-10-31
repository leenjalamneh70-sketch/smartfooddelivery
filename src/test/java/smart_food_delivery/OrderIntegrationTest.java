
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

/**
 * Integration tests for the complete order delivery workflow.
 *
 * Tests the interaction between OrderService,
 * DeliveryService, driver selection, order status
 * transitions, and the Observer Pattern.
 */
class OrderIntegrationTest {

    private OrderService orderService;
    private DeliveryService deliveryService;

    private NotificationService notifications;
    private OrderObserver observer;

    private Restaurant restaurant;
    private MenuItem pizza;
    private Order order;
    private Driver driver;

    @BeforeEach
    void setup() {

        restaurant = new Restaurant(
            "Pizza House", true, 20, "City"
        );

        pizza = new MenuItem("Pizza", 30, true);
        restaurant.addMenuItem(pizza);

        orderService = new OrderService();

        order = orderService.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City"
        );

        notifications = mock(NotificationService.class);
        observer = mock(OrderObserver.class);

        deliveryService = new DeliveryService(
            new NearestDriverStrategy(),
            notifications
        );

        deliveryService.addObserver(observer);

        driver = new Driver("Ahmad", true, 3, 10);
    }

    @Test
    void testCompleteOrderDeliveryWorkflow() {

        // Step 1: Order is created.
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNull(order.getAssignedDriver());

        // Step 2: Confirm the order.
        deliveryService.updateOrderStatus(
            order, OrderStatus.CONFIRMED
        );

        assertEquals(
            OrderStatus.CONFIRMED, order.getStatus()
        );

        // Step 3: Start preparing the order.
        deliveryService.updateOrderStatus(
            order, OrderStatus.PREPARING
        );

        assertEquals(
            OrderStatus.PREPARING, order.getStatus()
        );

        // Step 4: Order is ready.
        deliveryService.updateOrderStatus(
            order, OrderStatus.READY
        );

        assertEquals(
            OrderStatus.READY, order.getStatus()
        );

        // Step 5: Assign an available driver.
        boolean assigned = deliveryService.assignDriver(
            order, Arrays.asList(driver)
        );

        assertTrue(assigned);
        assertSame(driver, order.getAssignedDriver());
        assertFalse(driver.isAvailable());

        // Step 6: Order goes out for delivery.
        deliveryService.updateOrderStatus(
            order, OrderStatus.OUT_FOR_DELIVERY
        );

        assertEquals(
            OrderStatus.OUT_FOR_DELIVERY,
            order.getStatus()
        );

        // Step 7: Order is delivered.
        deliveryService.updateOrderStatus(
            order, OrderStatus.DELIVERED
        );

        assertEquals(
            OrderStatus.DELIVERED,
            order.getStatus()
        );

        // Step 8: Driver becomes available again.
        assertTrue(driver.isAvailable());

        // Step 9: Verify notification service.
        verify(notifications, times(6))
            .sendNotification(anyString());

        // Step 10: Verify Observer Pattern.
        verify(observer, times(6))
            .update(anyString());

        verify(observer).update(
            "Order #" + order.getId()
            + " status changed to DELIVERED"
        );

        verify(observer).update(
            "Driver Ahmad assigned to order #"
            + order.getId()
        );
    }

    @Test
    void testCannotDeliverWithoutAssignedDriver() {

        deliveryService.updateOrderStatus(
            order, OrderStatus.CONFIRMED
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.PREPARING
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.READY
        );

        assertThrows(IllegalStateException.class, () ->
            deliveryService.updateOrderStatus(
                order, OrderStatus.OUT_FOR_DELIVERY
            )
        );

        assertEquals(
            OrderStatus.READY, order.getStatus()
        );

        verify(observer, times(3))
            .update(anyString());
    }

    @Test
    void testDeliveredOrderCannotBeModified() {

        deliveryService.assignDriver(
            order, Arrays.asList(driver)
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.CONFIRMED
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.PREPARING
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.READY
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.OUT_FOR_DELIVERY
        );

        deliveryService.updateOrderStatus(
            order, OrderStatus.DELIVERED
        );

        assertThrows(IllegalStateException.class, () ->
            deliveryService.updateOrderStatus(
                order, OrderStatus.CONFIRMED
            )
        );

        assertEquals(
            OrderStatus.DELIVERED, order.getStatus()
        );

        assertTrue(driver.isAvailable());
    }

    @Test
    void testObserverRemovalDuringOrderWorkflow() {

        deliveryService.updateOrderStatus(
            order, OrderStatus.CONFIRMED
        );

        verify(observer).update(
            "Order #" + order.getId()
            + " status changed to CONFIRMED"
        );

        deliveryService.removeObserver(observer);

        deliveryService.updateOrderStatus(
            order, OrderStatus.PREPARING
        );

        // Observer received only the first notification.
        verify(observer, times(1))
            .update(anyString());

        // Original notification service still works.
        verify(notifications, times(2))
            .sendNotification(anyString());
    }

    @Test
    void testDriverCannotBeAssignedTwice() {

        assertTrue(deliveryService.assignDriver(
            order, Arrays.asList(driver)
        ));

        assertFalse(deliveryService.assignDriver(
            order, Arrays.asList(driver)
        ));

        assertSame(driver, order.getAssignedDriver());

        verify(notifications, times(1))
            .sendNotification(anyString());

        verify(observer, times(1))
            .update(anyString());
    }
}


package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class DeliveryServiceTest {

    private Order order;
    private NotificationService notifications;
    private DeliveryService service;

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
                new NearestDriverStrategy(), notifications);
    }

    @Test
    void testNearestDriverAssignment() {

        Driver far = new Driver("Ali", true, 10, 12);
        Driver near = new Driver("Sara", true, 2, 20);

        assertTrue(service.assignDriver(
                order, Arrays.asList(far, near)));

        assertSame(near, order.getAssignedDriver());
        assertFalse(near.isAvailable());

        verify(notifications).sendNotification(
                "Driver Sara assigned to order #" + order.getId());
    }

    @Test
    void testFastestDriverSelection() {

        Driver first = new Driver("Ali", true, 2, 20);
        Driver second = new Driver("Sara", true, 5, 8);

        DeliveryService fastestService = new DeliveryService(
                new FastestDriverStrategy(), notifications);

        assertTrue(fastestService.assignDriver(
                order, Arrays.asList(first, second)));

        assertSame(second, order.getAssignedDriver());
    }

    @Test
    void testBusyDriverRejected() {

        Driver busy = new Driver("Ali", false, 1, 5);

        assertFalse(service.assignDriver(
                order, Arrays.asList(busy)));

        assertNull(order.getAssignedDriver());
        verifyNoInteractions(notifications);
    }

    @Test
    void testOrderStatusNotification() {

        service.updateOrderStatus(
                order, OrderStatus.CONFIRMED);

        assertEquals(
                OrderStatus.CONFIRMED, order.getStatus());

        verify(notifications).sendNotification(
                "Order #" + order.getId()
                + " status changed to CONFIRMED");
    }

    @Test
    void testInvalidStatusTransition() {

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.DELIVERED));

        assertEquals(
                OrderStatus.PENDING, order.getStatus());

        verifyNoInteractions(notifications);
    }

    @Test
    void testDeliveryNotifications() {

        Driver driver = new Driver("Ali", true, 3, 10);

        assertTrue(service.assignDriver(
                order, Arrays.asList(driver)));

        service.updateOrderStatus(
                order, OrderStatus.CONFIRMED);
        service.updateOrderStatus(
                order, OrderStatus.PREPARING);
        service.updateOrderStatus(
                order, OrderStatus.READY);
        service.updateOrderStatus(
                order, OrderStatus.OUT_FOR_DELIVERY);
        service.updateOrderStatus(
                order, OrderStatus.DELIVERED);

        assertEquals(
                OrderStatus.DELIVERED, order.getStatus());

        assertTrue(driver.isAvailable());

        verify(notifications).sendNotification(
                "Order #" + order.getId()
                + " status changed to OUT_FOR_DELIVERY");

        verify(notifications).sendNotification(
                "Order #" + order.getId()
                + " status changed to DELIVERED");
    }

    // Constructor validation

    @Test
    void testNullStrategyRejected() {

        assertThrows(IllegalArgumentException.class, () ->
                new DeliveryService(null, notifications));
    }

    @Test
    void testNullNotificationServiceRejected() {

        assertThrows(IllegalArgumentException.class, () ->
                new DeliveryService(
                        new NearestDriverStrategy(), null));
    }

    // Driver assignment validation

    @Test
    void testNullOrderAssignmentRejected() {

        assertFalse(service.assignDriver(
                null, Collections.emptyList()));

        verifyNoInteractions(notifications);
    }

    @Test
    void testNullDriverListRejected() {

        assertFalse(service.assignDriver(order, null));

        verifyNoInteractions(notifications);
    }

    @Test
    void testEmptyDriverListRejected() {

        assertFalse(service.assignDriver(
                order, Collections.emptyList()));

        verifyNoInteractions(notifications);
    }

    @Test
    void testNullDriverIgnored() {

        Driver valid = new Driver("Sara", true, 2, 10);

        assertTrue(service.assignDriver(
                order, Arrays.asList(null, valid)));

        assertSame(valid, order.getAssignedDriver());
    }

    @Test
    void testOnlyNullDriversRejected() {

        assertFalse(service.assignDriver(
                order, Arrays.asList((Driver) null)));

        verifyNoInteractions(notifications);
    }

    @Test
    void testCancelledOrderAssignmentRejected() {

        order.setStatus(OrderStatus.CANCELLED);

        assertFalse(service.assignDriver(
                order,
                Arrays.asList(
                        new Driver("Ali", true, 2, 10))));

        verifyNoInteractions(notifications);
    }

    @Test
    void testDeliveredOrderAssignmentRejected() {

        order.setStatus(OrderStatus.DELIVERED);

        assertFalse(service.assignDriver(
                order,
                Arrays.asList(
                        new Driver("Ali", true, 2, 10))));

        verifyNoInteractions(notifications);
    }

    @Test
    void testAlreadyAssignedOrderRejected() {

        Driver first = new Driver("Ali", true, 2, 10);
        Driver second = new Driver("Sara", true, 3, 8);

        assertTrue(service.assignDriver(
                order, Arrays.asList(first)));

        assertFalse(service.assignDriver(
                order, Arrays.asList(second)));

        assertSame(first, order.getAssignedDriver());
        verify(notifications, times(1))
                .sendNotification(anyString());
    }

    @Test
    void testStrategyReturnsNull() {

        DriverSelectionStrategy strategy =
                mock(DriverSelectionStrategy.class);

        when(strategy.selectDriver(anyList()))
                .thenReturn(null);

        DeliveryService customService =
                new DeliveryService(strategy, notifications);

        Driver driver = new Driver("Ali", true, 2, 10);

        assertFalse(customService.assignDriver(
                order, Arrays.asList(driver)));

        assertNull(order.getAssignedDriver());
        verifyNoInteractions(notifications);
    }

    @Test
    void testStrategyReturnsUnknownDriver() {

        DriverSelectionStrategy strategy =
                mock(DriverSelectionStrategy.class);

        Driver unknown = new Driver("Unknown", true, 2, 10);

        when(strategy.selectDriver(anyList()))
                .thenReturn(unknown);

        DeliveryService customService =
                new DeliveryService(strategy, notifications);

        Driver candidate = new Driver("Ali", true, 3, 10);

        assertFalse(customService.assignDriver(
                order, Arrays.asList(candidate)));

        assertNull(order.getAssignedDriver());
        verifyNoInteractions(notifications);
    }

    @Test
    void testStrategyReturnsIneligibleDriver() {

        DriverSelectionStrategy strategy =
                mock(DriverSelectionStrategy.class);

        Driver driver = new Driver("Ali", true, 2, 10);

        when(strategy.selectDriver(anyList()))
                .thenAnswer(invocation -> {
                    driver.setAvailable(false);
                    return driver;
                });

        DeliveryService customService =
                new DeliveryService(strategy, notifications);

        assertFalse(customService.assignDriver(
                order, Arrays.asList(driver)));

        assertNull(order.getAssignedDriver());
        verifyNoInteractions(notifications);
    }

    // Order status validation

    @Test
    void testNullOrderStatusUpdateRejected() {

        assertThrows(IllegalArgumentException.class, () ->
                service.updateOrderStatus(
                        null, OrderStatus.CONFIRMED));
    }

    @Test
    void testNullNewStatusRejected() {

        assertThrows(IllegalArgumentException.class, () ->
                service.updateOrderStatus(order, null));
    }

    @Test
    void testCancelledOrderCannotChangeStatus() {

        order.setStatus(OrderStatus.CANCELLED);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.CONFIRMED));
    }

    @Test
    void testDeliveredOrderCannotChangeStatus() {

        order.setStatus(OrderStatus.DELIVERED);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.CONFIRMED));
    }

    @Test
    void testConfirmedCannotSkipToReady() {

        order.setStatus(OrderStatus.CONFIRMED);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.READY));
    }

    @Test
    void testPreparingCannotSkipToDelivered() {

        order.setStatus(OrderStatus.PREPARING);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.DELIVERED));
    }

    @Test
    void testReadyCannotDeliverWithoutDriver() {

        order.setStatus(OrderStatus.READY);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.OUT_FOR_DELIVERY));

        assertEquals(OrderStatus.READY, order.getStatus());
    }

    @Test
    void testOutForDeliveryCannotGoBackToReady() {

        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        assertThrows(IllegalStateException.class, () ->
                service.updateOrderStatus(
                        order, OrderStatus.READY));
    }
}

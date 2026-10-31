
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.Driver;
import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.OrderStatus;

import smart_food_delivery.service.DeliveryService;
import smart_food_delivery.service.DriverSelectionStrategy;
import smart_food_delivery.service.NotificationService;
import smart_food_delivery.service.OrderObserver;

class ObserverPatternTest {

    private DeliveryService deliveryService;
    private NotificationService notificationService;
    private OrderObserver observer1;
    private OrderObserver observer2;
    private Order order;

    @BeforeEach
    void setup() {

        DriverSelectionStrategy strategy =
            mock(DriverSelectionStrategy.class);

        notificationService =
            mock(NotificationService.class);

        observer1 = mock(OrderObserver.class);
        observer2 = mock(OrderObserver.class);

        order = mock(Order.class);

        when(order.getStatus())
            .thenReturn(OrderStatus.PENDING);

        when(order.getId())
            .thenReturn(1);

        deliveryService = new DeliveryService(
            strategy,
            notificationService
        );
    }

    @Test
    void testObserverReceivesNotification() {

        deliveryService.addObserver(observer1);

        deliveryService.updateOrderStatus(
            order,
            OrderStatus.CONFIRMED
        );

        verify(observer1).update(
            "Order #1 status changed to CONFIRMED"
        );

        verify(notificationService).sendNotification(
            "Order #1 status changed to CONFIRMED"
        );
    }

    @Test
    void testMultipleObserversReceiveNotification() {

        deliveryService.addObserver(observer1);
        deliveryService.addObserver(observer2);

        deliveryService.updateOrderStatus(
            order,
            OrderStatus.CONFIRMED
        );

        verify(observer1).update(
            "Order #1 status changed to CONFIRMED"
        );

        verify(observer2).update(
            "Order #1 status changed to CONFIRMED"
        );
    }

    @Test
    void testRemovedObserverDoesNotReceiveNotification() {

        deliveryService.addObserver(observer1);
        deliveryService.addObserver(observer2);

        deliveryService.removeObserver(observer1);

        deliveryService.updateOrderStatus(
            order,
            OrderStatus.CONFIRMED
        );

        verifyNoInteractions(observer1);

        verify(observer2).update(
            "Order #1 status changed to CONFIRMED"
        );
    }

    @Test
    void testDuplicateObserverReceivesOnlyOneNotification() {

        deliveryService.addObserver(observer1);
        deliveryService.addObserver(observer1);

        deliveryService.updateOrderStatus(
            order,
            OrderStatus.CONFIRMED
        );

        verify(observer1, times(1)).update(
            "Order #1 status changed to CONFIRMED"
        );
    }

    @Test
    void testNullObserverRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            deliveryService.addObserver(null)
        );
    }

    @Test
    void testNoObserversStillUsesNotificationService() {

        deliveryService.updateOrderStatus(
            order,
            OrderStatus.CONFIRMED
        );

        verify(notificationService).sendNotification(
            "Order #1 status changed to CONFIRMED"
        );
    }

    @Test
    void testInvalidStatusDoesNotNotifyObservers() {

        deliveryService.addObserver(observer1);

        assertThrows(IllegalStateException.class, () ->
            deliveryService.updateOrderStatus(
                order,
                OrderStatus.DELIVERED
            )
        );

        verifyNoInteractions(observer1);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testObserverReceivesDriverAssignmentNotification() {

        Driver driver = mock(Driver.class);

        when(driver.canDeliver(anyDouble(), anyInt()))
            .thenReturn(true);

        when(driver.getName())
            .thenReturn("Ahmad");

        when(order.getDeliveryDistance())
            .thenReturn(5.0);

        when(order.getItems())
            .thenReturn(Collections.emptyList());

        DriverSelectionStrategy strategy =
            mock(DriverSelectionStrategy.class);

        when(strategy.selectDriver(anyList()))
            .thenReturn(driver);

        DeliveryService service = new DeliveryService(
            strategy,
            notificationService
        );

        service.addObserver(observer1);

        boolean assigned = service.assignDriver(
            order,
            Arrays.asList(driver)
        );

        assertTrue(assigned);

        verify(observer1).update(
            "Driver Ahmad assigned to order #1"
        );

        verify(notificationService).sendNotification(
            "Driver Ahmad assigned to order #1"
        );

        verify(driver).setAvailable(false);
    }
}

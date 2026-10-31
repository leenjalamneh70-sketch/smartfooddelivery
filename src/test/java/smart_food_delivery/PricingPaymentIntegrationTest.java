
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

/**
 * Integration tests for order creation, pricing,
 * delivery, and cash payment.
 *
 * Tests the interaction between:
 * OrderService, PricingService, DeliveryService,
 * PaymentService, and NotificationService.
 */
class PricingPaymentIntegrationTest {

    private OrderService orderService;
    private PricingService pricingService;
    private DeliveryService deliveryService;
    private PaymentService paymentService;

    private NotificationService notifications;

    private Restaurant restaurant;
    private MenuItem pizza;
    private Order order;
    private Driver driver;

    private PricingStrategy pricingStrategy;

    @BeforeEach
    void setup() {

        restaurant = new Restaurant(
            "Pizza House", true, 20, "City"
        );

        pizza = new MenuItem("Pizza", 30, true);
        restaurant.addMenuItem(pizza);

        orderService = new OrderService();
        pricingService = new PricingService();
        paymentService = new PaymentService();

        notifications = mock(NotificationService.class);

        deliveryService = new DeliveryService(
            new NearestDriverStrategy(),
            notifications
        );

        order = orderService.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City"
        );

        driver = new Driver("Ahmad", true, 3, 10);

        // No price adjustment: keeps the food subtotal.
        pricingStrategy = mock(PricingStrategy.class);

        when(pricingStrategy.calculatePrice(anyDouble()))
            .thenAnswer(invocation ->
                invocation.getArgument(0, Double.class)
            );
    }

    /**
     * Completes the entire workflow:
     * create -> price -> assign -> deliver -> pay.
     */
    @Test
    void testCompletePricingDeliveryPaymentWorkflow() {

        // Step 1: Verify the newly created order.
        assertNotNull(order);
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(60.0, order.getTotal(), 0.001);

        // Step 2: Calculate and save the final bill.
        double finalBill = pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        assertTrue(finalBill >= order.getTotal());

        assertEquals(
            finalBill, order.getFinalBill(), 0.001
        );

        assertEquals(
            3.0, order.getDeliveryDistance(), 0.001
        );

        // Step 3: Assign a driver.
        assertTrue(deliveryService.assignDriver(
            order, Arrays.asList(driver)
        ));

        assertSame(driver, order.getAssignedDriver());
        assertFalse(driver.isAvailable());

        // Step 4: Move through all delivery statuses.
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

        assertEquals(
            OrderStatus.DELIVERED, order.getStatus()
        );

        assertTrue(driver.isAvailable());

        // Step 5: Pay the exact final bill.
        boolean paid = paymentService.processCashPayment(
            order, finalBill
        );

        assertTrue(paid);

        assertEquals(
            PaymentStatus.PAID, order.getPaymentStatus()
        );

        // Step 6: Verify that the payment method was saved.
        assertEquals("Cash", order.getPaymentMethod());

        // Step 7: Verify delivery notifications.
        verify(notifications, times(6))
            .sendNotification(anyString());
    }

    /**
     * Payment must be rejected before delivery.
     */
    @Test
    void testPaymentRejectedBeforeDelivery() {

        double finalBill = pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        assertFalse(paymentService.processCashPayment(
            order, finalBill
        ));

        assertNotEquals(
            PaymentStatus.PAID, order.getPaymentStatus()
        );
    }

    /**
     * Payment with an incorrect amount must fail.
     */
    @Test
    void testIncorrectPaymentAmountRejected() {

        double finalBill = pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        completeDelivery();

        assertFalse(paymentService.processCashPayment(
            order, finalBill - 5.0
        ));

        assertNotEquals(
            PaymentStatus.PAID, order.getPaymentStatus()
        );
    }

    /**
     * A successfully paid order cannot be paid again.
     */
    @Test
    void testDuplicatePaymentRejected() {

        double finalBill = pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        completeDelivery();

        assertTrue(paymentService.processCashPayment(
            order, finalBill
        ));

        assertFalse(paymentService.processCashPayment(
            order, finalBill
        ));

        assertEquals(
            PaymentStatus.PAID, order.getPaymentStatus()
        );
    }

    /**
     * Delivered orders cannot have their bill recalculated.
     */
    @Test
    void testPricingRejectedAfterDelivery() {

        pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        completeDelivery();

        assertThrows(IllegalStateException.class, () ->
            pricingService.calculateFinalBill(
                order, pricingStrategy, 3.0
            )
        );
    }

    /**
     * Invalid payment amounts must be rejected.
     */
    @Test
    void testInvalidPaymentAmountsRejected() {

        double finalBill = pricingService.calculateFinalBill(
            order, pricingStrategy, 3.0
        );

        completeDelivery();

        assertFalse(paymentService.processCashPayment(
            order, Double.NaN
        ));

        assertFalse(paymentService.processCashPayment(
            order, Double.POSITIVE_INFINITY
        ));

        assertFalse(paymentService.processCashPayment(
            order, -1.0
        ));

        // Valid payment should still work afterward.
        assertTrue(paymentService.processCashPayment(
            order, finalBill
        ));
    }

    /**
     * Helper method for completing the delivery workflow.
     */
    private void completeDelivery() {

        assertTrue(deliveryService.assignDriver(
            order, Arrays.asList(driver)
        ));

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
    }
}

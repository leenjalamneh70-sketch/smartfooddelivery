
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class PaymentServiceTest {

    private Order order;
    private PaymentService paymentService;

    @BeforeEach
    void setup() {

        Restaurant restaurant =
            new Restaurant("Pizza House", true, 20, "City");

        MenuItem pizza = new MenuItem("Pizza", 50, true);
        restaurant.addMenuItem(pizza);

        OrderService orderService = new OrderService();

        order = orderService.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City"
        );

        PricingService pricingService = new PricingService();

        pricingService.calculateFinalBill(
            order,
            new NormalPricing(),
            5
        );

        paymentService = new PaymentService();
    }

    @Test
    void testValidCashPayment() {

        order.setStatus(OrderStatus.DELIVERED);

        assertTrue(paymentService.processCashPayment(order, 110));
        assertEquals(PaymentStatus.PAID, order.getPaymentStatus());
        assertEquals("Cash", order.getPaymentMethod());
    }

    @Test
    void testInvalidPaymentAmount() {

        order.setStatus(OrderStatus.DELIVERED);

        assertFalse(paymentService.processCashPayment(order, 50));
        assertEquals(PaymentStatus.UNPAID, order.getPaymentStatus());
    }

    @Test
    void testPaymentBeforeDeliveryRejected() {

        assertFalse(paymentService.processCashPayment(order, 110));
        assertEquals(PaymentStatus.UNPAID, order.getPaymentStatus());
    }

    @Test
    void testDuplicatePaymentRejected() {

        order.setStatus(OrderStatus.DELIVERED);

        assertTrue(paymentService.processCashPayment(order, 110));
        assertFalse(paymentService.processCashPayment(order, 110));
    }

    @Test
    void testNullOrderRejected() {

        assertFalse(paymentService.processCashPayment(null, 110));
    }
}

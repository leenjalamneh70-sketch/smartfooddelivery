
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class PricingServiceTest {

    private Order order;
    private PricingService service;
    private Restaurant restaurant;
    private MenuItem pizza;
    private OrderService orderService;

    @BeforeEach
    void setup() {

        restaurant =
            new Restaurant("Pizza House", true, 20, "City");

        pizza = new MenuItem("Pizza", 50, true);
        restaurant.addMenuItem(pizza);

        orderService = new OrderService();

        order = orderService.placeOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City"
        );

        service = new PricingService();
    }

    private ExpressOrder createExpressOrder(double expressFee) {

        return orderService.placeExpressOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City",
            expressFee
        );
    }

    @Test
    void testNormalPricing() {

        double result = service.calculateFinalBill(
            order, new NormalPricing(), 5
        );

        assertEquals(110, result, 0.001);
        assertEquals(10, order.getDeliveryFee(), 0.001);
        assertEquals(110, order.getFinalBill(), 0.001);
    }

    @Test
    void testPeakHourPricing() {

        double result = service.calculateFinalBill(
            order, new PeakHourPricing(), 5
        );

        assertEquals(130, result, 0.001);
    }

    @Test
    void testExpressPricing() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                order, new ExpressPricing(), 5
            )
        );
    }

    @Test
    void testPromotionalPricing() {

        double result = service.calculateFinalBill(
            order, new PromotionalPricing(), 5
        );

        assertEquals(100, result, 0.001);
    }

    @Test
    void testInvalidDistance() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                order, new NormalPricing(), -5
            )
        );
    }

    @Test
    void testDeliveredOrderCannotBeRepriced() {

        order.setStatus(OrderStatus.DELIVERED);

        assertThrows(IllegalStateException.class, () ->
            service.calculateFinalBill(
                order, new NormalPricing(), 5
            )
        );
    }

    @Test
    void testValidExpressPricing() {

        ExpressOrder expressOrder = createExpressOrder(10.0);

        double result = service.calculateFinalBill(
            expressOrder, new ExpressPricing(), 5
        );

        assertEquals(120.0, result, 0.001);
        assertEquals(120.0, expressOrder.getFinalBill(), 0.001);
        assertEquals(10.0, expressOrder.getDeliveryFee(), 0.001);
    }

    // Additional validation tests

    @Test
    void testNullOrderRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                null, new NormalPricing(), 5
            )
        );
    }

    @Test
    void testNullStrategyRejected() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(order, null, 5)
        );
    }

    @Test
    void testCancelledOrderCannotBeRepriced() {

        order.setStatus(OrderStatus.CANCELLED);

        assertThrows(IllegalStateException.class, () ->
            service.calculateFinalBill(
                order, new NormalPricing(), 5
            )
        );
    }

    @Test
    void testPaidOrderCannotBeRepriced() {

        order.markAsPaid("CASH");

        assertEquals(
            PaymentStatus.PAID,
            order.getPaymentStatus()
        );

        assertThrows(IllegalStateException.class, () ->
            service.calculateFinalBill(
                order, new NormalPricing(), 5
            )
        );
    }

    @Test
    void testNegativeCalculatedPriceRejected() {

        PricingStrategy negativeStrategy =
            mock(PricingStrategy.class);

        when(negativeStrategy.calculatePrice(anyDouble()))
            .thenReturn(-1.0);

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                order, negativeStrategy, 5
            )
        );
    }

    @Test
    void testNaNCalculatedPriceRejected() {

        PricingStrategy invalidStrategy =
            mock(PricingStrategy.class);

        when(invalidStrategy.calculatePrice(anyDouble()))
            .thenReturn(Double.NaN);

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                order, invalidStrategy, 5
            )
        );
    }

    @Test
    void testInfiniteCalculatedPriceRejected() {

        PricingStrategy invalidStrategy =
            mock(PricingStrategy.class);

        when(invalidStrategy.calculatePrice(anyDouble()))
            .thenReturn(Double.POSITIVE_INFINITY);

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                order, invalidStrategy, 5
            )
        );
    }

    @Test
    void testExpressOrderWithNormalPricing() {

        ExpressOrder expressOrder = createExpressOrder(15.0);

        double result = service.calculateFinalBill(
            expressOrder, new NormalPricing(), 5
        );

        assertEquals(125.0, result, 0.001);
        assertEquals(25.0, expressOrder.getDeliveryFee(), 0.001);
        assertEquals(125.0, expressOrder.getFinalBill(), 0.001);
    }

    @Test
    void testExpressPricingFeeMismatchRejected() {

        ExpressOrder expressOrder = createExpressOrder(15.0);

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                expressOrder, new ExpressPricing(), 5
            )
        );
    }

    @Test
    void testInfiniteFinalPriceRejected() {

        PricingStrategy largeStrategy =
            mock(PricingStrategy.class);

        when(largeStrategy.calculatePrice(anyDouble()))
            .thenReturn(Double.MAX_VALUE);

        ExpressOrder expressOrder =
            createExpressOrder(Double.MAX_VALUE);

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(
                expressOrder, largeStrategy, 5
            )
        );
    }
}

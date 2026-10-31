
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;
import smart_food_delivery.service.*;

class PricingServiceTest {

    private Order order;
    private PricingService service;

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

        service = new PricingService();
    }

    @Test
    void testNormalPricing() {

        double result =
            service.calculateFinalBill(order, new NormalPricing(), 5);

        assertEquals(110, result, 0.001);
        assertEquals(10, order.getDeliveryFee(), 0.001);
        assertEquals(110, order.getFinalBill(), 0.001);
    }

    @Test
    void testPeakHourPricing() {

        double result =
            service.calculateFinalBill(order, new PeakHourPricing(), 5);

        assertEquals(130, result, 0.001);
    }

    @Test
    void testExpressPricing() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(order, new ExpressPricing(), 5)
        );
    }

    @Test
    void testPromotionalPricing() {

        double result =
            service.calculateFinalBill(order, new PromotionalPricing(), 5);

        assertEquals(100, result, 0.001);
    }

    @Test
    void testInvalidDistance() {

        assertThrows(IllegalArgumentException.class, () ->
            service.calculateFinalBill(order, new NormalPricing(), -5)
        );
    }

    @Test
    void testDeliveredOrderCannotBeRepriced() {

        order.setStatus(OrderStatus.DELIVERED);

        assertThrows(IllegalStateException.class, () ->
            service.calculateFinalBill(order, new NormalPricing(), 5)
        );
    }
    @Test
    void testValidExpressPricing() {

        Restaurant restaurant =
            new Restaurant("Pizza House", true, 20, "City");

        MenuItem pizza = new MenuItem("Pizza", 50, true);
        restaurant.addMenuItem(pizza);

        OrderService orderService = new OrderService();

        ExpressOrder expressOrder = orderService.placeExpressOrder(
            restaurant,
            Arrays.asList(new OrderItem(pizza, 2)),
            "Main Street",
            "City",
            10.0
        );

        double result = service.calculateFinalBill(
            expressOrder,
            new ExpressPricing(),
            5
        );

        assertEquals(120.0, result, 0.001);
        assertEquals(120.0, expressOrder.getFinalBill(), 0.001);
        assertEquals(10.0, expressOrder.getDeliveryFee(), 0.001);
    }
}

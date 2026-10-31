
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;

class ScheduledOrderTest {

    private Restaurant restaurant;
    private List<OrderItem> items;
    private Clock fixedClock;
    private LocalDateTime now;

    @BeforeEach
    void setup() {
        restaurant = new Restaurant("Pizza House", true, 20, "City");

        MenuItem pizza = new MenuItem("Pizza", 50, true);
        restaurant.addMenuItem(pizza);

        items = Arrays.asList(new OrderItem(pizza, 2));

        fixedClock = Clock.fixed(
            Instant.parse("2026-10-31T12:00:00Z"),
            ZoneOffset.UTC
        );

        now = LocalDateTime.now(fixedClock);
    }

    private ScheduledOrder createOrder(
            LocalDateTime time, Clock clock) {

        return new ScheduledOrder(
            1,
            restaurant,
            items,
            "Main Street",
            "City",
            time,
            clock
        );
    }

    @Test
    void testValidFutureScheduledTime() {
        LocalDateTime future = now.plusDays(1);

        ScheduledOrder order = createOrder(future, fixedClock);

        assertEquals(future, order.getScheduledTime());
    }

    @Test
    void testScheduledTimeOneSecondInFuture() {
        LocalDateTime future = now.plusSeconds(1);

        ScheduledOrder order = createOrder(future, fixedClock);

        assertEquals(future, order.getScheduledTime());
    }

    @Test
    void testScheduledTimeInPast() {
        assertThrows(IllegalArgumentException.class, () ->
            createOrder(now.minusDays(1), fixedClock)
        );
    }

    @Test
    void testScheduledTimeEqualToNow() {
        assertThrows(IllegalArgumentException.class, () ->
            createOrder(now, fixedClock)
        );
    }

    @Test
    void testNullScheduledTime() {
        assertThrows(IllegalArgumentException.class, () ->
            createOrder(null, fixedClock)
        );
    }

    @Test
    void testNullClock() {
        assertThrows(IllegalArgumentException.class, () ->
            createOrder(now.plusDays(1), null)
        );
    }

    @Test
    void testConstructorUsingSystemClock() {
        LocalDateTime future = LocalDateTime.now().plusDays(1);

        ScheduledOrder order = new ScheduledOrder(
            2,
            restaurant,
            items,
            "Main Street",
            "City",
            future
        );

        assertEquals(future, order.getScheduledTime());
    }
}

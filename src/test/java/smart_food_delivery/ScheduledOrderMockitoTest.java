
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.*;

/**
 * Tests scheduled order time validation using Mockito.
 *
 * A mocked Clock controls the current date and time
 * without depending on the real system clock.
 */
class ScheduledOrderMockitoTest {

    private Restaurant restaurant;
    private List<OrderItem> items;
    private Clock mockClock;
    private LocalDateTime now;

    @BeforeEach
    void setup() {

        restaurant = new Restaurant(
                "Pizza House", true, 20, "City");

        MenuItem pizza = new MenuItem(
                "Pizza", 50, true);

        restaurant.addMenuItem(pizza);

        items = Arrays.asList(
                new OrderItem(pizza, 2));

        mockClock = mock(Clock.class);

        when(mockClock.instant()).thenReturn(
                Instant.parse("2026-10-31T12:00:00Z"));

        when(mockClock.getZone()).thenReturn(
                ZoneOffset.UTC);

        now = LocalDateTime.now(mockClock);
    }

    private ScheduledOrder createOrder(
            LocalDateTime scheduledTime) {

        return new ScheduledOrder(
                1,
                restaurant,
                items,
                "Main Street",
                "City",
                scheduledTime,
                mockClock);
    }

    @Test
    void testFutureTimeAccepted() {

        LocalDateTime future = now.plusDays(1);

        ScheduledOrder order = createOrder(future);

        assertEquals(
                future,
                order.getScheduledTime());
    }

    @Test
    void testPastTimeRejected() {

        LocalDateTime past = now.minusDays(1);

        assertThrows(
                IllegalArgumentException.class,
                () -> createOrder(past));
    }

    @Test
    void testCurrentTimeRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> createOrder(now));
    }

    @Test
    void testOneSecondInFutureAccepted() {

        LocalDateTime future = now.plusSeconds(1);

        ScheduledOrder order = createOrder(future);

        assertEquals(
                future,
                order.getScheduledTime());
    }

    @Test
    void testMockClockUsed() {

        createOrder(now.plusHours(2));

        verify(mockClock, atLeastOnce()).instant();
        verify(mockClock, atLeastOnce()).getZone();
    }
}

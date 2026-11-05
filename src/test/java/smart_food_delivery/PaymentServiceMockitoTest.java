
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.Order;
import smart_food_delivery.domain.OrderStatus;
import smart_food_delivery.domain.PaymentStatus;
import smart_food_delivery.service.PaymentService;

/**
 * Tests cash payment processing using Mockito.
 *
 * A mocked Order is used to verify successful
 * payments and the rejection of invalid payments
 * without modifying real order data.
 */
class PaymentServiceMockitoTest {

    private PaymentService paymentService;
    private Order mockOrder;

    @BeforeEach
    void setup() {
        paymentService = new PaymentService();
        mockOrder = mock(Order.class);
    }

    @Test
    void testValidCashPaymentWithMockito() {

        when(mockOrder.getPaymentStatus())
                .thenReturn(PaymentStatus.UNPAID);

        when(mockOrder.getStatus())
                .thenReturn(OrderStatus.DELIVERED);

        when(mockOrder.getFinalBill())
                .thenReturn(100.0);

        boolean result =
                paymentService.processCashPayment(
                        mockOrder, 100.0);

        assertTrue(result);

        verify(mockOrder).markAsPaid("Cash");
    }

    @Test
    void testInvalidAmountWithMockito() {

        when(mockOrder.getPaymentStatus())
                .thenReturn(PaymentStatus.UNPAID);

        when(mockOrder.getStatus())
                .thenReturn(OrderStatus.DELIVERED);

        when(mockOrder.getFinalBill())
                .thenReturn(100.0);

        boolean result =
                paymentService.processCashPayment(
                        mockOrder, 50.0);

        assertFalse(result);

        verify(mockOrder, never())
                .markAsPaid(anyString());
    }

    @Test
    void testPaymentBeforeDeliveryWithMockito() {

        when(mockOrder.getPaymentStatus())
                .thenReturn(PaymentStatus.UNPAID);

        when(mockOrder.getStatus())
                .thenReturn(OrderStatus.PENDING);

        boolean result =
                paymentService.processCashPayment(
                        mockOrder, 100.0);

        assertFalse(result);

        verify(mockOrder, never())
                .markAsPaid(anyString());
    }

    @Test
    void testAlreadyPaidOrderWithMockito() {

        when(mockOrder.getPaymentStatus())
                .thenReturn(PaymentStatus.PAID);

        boolean result =
                paymentService.processCashPayment(
                        mockOrder, 100.0);

        assertFalse(result);

        verify(mockOrder, never())
                .markAsPaid(anyString());
    }

    @Test
    void testNullOrderWithMockito() {

        boolean result =
                paymentService.processCashPayment(
                        null, 100.0);

        assertFalse(result);
    }
}

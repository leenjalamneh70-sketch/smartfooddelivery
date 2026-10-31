
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import smart_food_delivery.service.ConsoleNotificationService;
import smart_food_delivery.service.NotificationService;

class ConsoleNotificationServiceTest {

    @Test
    void testSendNotification() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, "UTF-8"));

            NotificationService service =
                new ConsoleNotificationService();

            service.sendNotification("Hello");

            String actual = new String(
                output.toByteArray(),
                StandardCharsets.UTF_8
            );

            assertEquals(
                "Notification: Hello" + System.lineSeparator(),
                actual
            );

        } catch (java.io.UnsupportedEncodingException e) {
            fail("UTF-8 encoding is not supported");

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testEmptyNotification() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, "UTF-8"));

            ConsoleNotificationService service =
                new ConsoleNotificationService();

            service.sendNotification("");

            String actual = new String(
                output.toByteArray(),
                StandardCharsets.UTF_8
            );

            assertEquals(
                "Notification: " + System.lineSeparator(),
                actual
            );

        } catch (java.io.UnsupportedEncodingException e) {
            fail("UTF-8 encoding is not supported");

        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void testNullNotification() {

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, "UTF-8"));

            ConsoleNotificationService service =
                new ConsoleNotificationService();

            service.sendNotification(null);

            String actual = new String(
                output.toByteArray(),
                StandardCharsets.UTF_8
            );

            assertEquals(
                "Notification: null" + System.lineSeparator(),
                actual
            );

        } catch (java.io.UnsupportedEncodingException e) {
            fail("UTF-8 encoding is not supported");

        } finally {
            System.setOut(originalOut);
        }
    }
}

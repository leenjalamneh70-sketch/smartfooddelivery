
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import smart_food_delivery.presentation.LoginUI;

class LoginUITest {

    private String runLogin(String username, String password) {

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        String userInput =
            username + System.lineSeparator()
            + password + System.lineSeparator();

        ByteArrayInputStream input =
            new ByteArrayInputStream(
                userInput.getBytes(StandardCharsets.UTF_8)
            );

        ByteArrayOutputStream output =
            new ByteArrayOutputStream();

        try {
            System.setIn(input);
            System.setOut(
                new PrintStream(output, true, "UTF-8")
            );

            LoginUI.main(new String[0]);

            return new String(
                output.toByteArray(),
                StandardCharsets.UTF_8
            );

        } catch (java.io.UnsupportedEncodingException e) {
            throw new AssertionError(
                "UTF-8 encoding is not supported", e
            );

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testSuccessfulLogin() {

        String output = runLogin("leen", "1234");

        assertTrue(output.contains("Enter username: "));
        assertTrue(output.contains("Enter password: "));
        assertTrue(output.contains("Login successful!"));
    }

    @Test
    void testInvalidPassword() {

        String output = runLogin("leen", "wrong");

        assertTrue(
            output.contains(
                "Error: Invalid username or password."
            )
        );
    }

    @Test
    void testInvalidUsername() {

        String output = runLogin("wrong", "1234");

        assertTrue(
            output.contains(
                "Error: Invalid username or password."
            )
        );
    }

    @Test
    void testBothCredentialsInvalid() {

        String output = runLogin("wrong", "wrong");

        assertTrue(
            output.contains(
                "Error: Invalid username or password."
            )
        );
    }
}

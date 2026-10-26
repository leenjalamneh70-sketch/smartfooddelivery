
package smart_food_delivery.presentation;

import smart_food_delivery.domain.User;
import smart_food_delivery.service.LoginService;
import java.util.Scanner;

/**
 * Provides a console-based login interface
 * for the Smart Food Delivery Management System.
 *
 * This class allows the user to enter a username
 * and password, validates the credentials using
 * LoginService, and displays the login result.
 */
public class LoginUI {

    /**
     * Starts the login interface.
     *
     * Creates a sample user, reads the entered
     * username and password from the console,
     * and checks the credentials using LoginService.
     *
     * Displays a success message if the credentials
     * are correct, or an error message otherwise.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        User user = new User("leen", "1234", "costumer");
        LoginService service = new LoginService();

        Scanner input = new Scanner(System.in);

        System.out.print("Enter username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = input.nextLine();

        boolean result = service.login(
            enteredUsername, enteredPassword, user
        );

        if (result) {
            System.out.println("Login successful!");
        } else {
            System.out.println(
                "Error: Invalid username or password."
            );
        }
    }
}

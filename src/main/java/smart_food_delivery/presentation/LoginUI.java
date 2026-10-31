
package smart_food_delivery.presentation;

import smart_food_delivery.service.LoginService;
import java.util.Scanner;

/**
 * Provides a console-based login interface
 * for the Smart Food Delivery Management System.
 *
 * Reads user credentials and validates them
 * against users stored in users.txt.
 */
public class LoginUI {

    /**
     * Starts the login interface.
     *
     * Reads the entered username and password,
     * validates them using LoginService,
     * and displays the login result.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        LoginService service = new LoginService();

        Scanner input = new Scanner(System.in);

        System.out.print("Enter username: ");
        String enteredUsername = input.nextLine();

        System.out.print("Enter password: ");
        String enteredPassword = input.nextLine();

        boolean result = service.login(
                enteredUsername, enteredPassword
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

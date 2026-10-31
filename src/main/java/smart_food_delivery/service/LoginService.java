
package smart_food_delivery.service;

import smart_food_delivery.domain.User;

/**
 * Provides user login functionality in the
 * Smart Food Delivery Management System.
 *
 * Validates login credentials by comparing the
 * entered username and password with the
 * stored user's information.
 */
public class LoginService {

    /**
     * Checks whether the provided login credentials
     * match the stored user's credentials.
     *
     * Returns false if the stored user is null
     * or the credentials do not match.
     *
     * @param username the entered username
     * @param password the entered password
     * @param storedUser the user whose credentials
     *        are stored in the system
     * @return true if the username and password match,
     *         otherwise false
     */
    public boolean login(String username, String password, User storedUser) {

        if (storedUser == null) {
            return false;
        }

        if (username.equals(storedUser.getUsername())
                && password.equals(storedUser.getPassword())) {
            return true;
        }

        return false;
    }
}

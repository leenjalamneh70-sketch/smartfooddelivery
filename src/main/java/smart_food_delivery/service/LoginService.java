
package smart_food_delivery.service;

import smart_food_delivery.domain.User;
import smart_food_delivery.repository.UserRepository;

/**
 * Provides user authentication functionality for the
 * Smart Food Delivery Management System.
 *
 * This service validates user credentials by comparing
 * the provided username and password with stored user data.
 *
 * It supports two authentication approaches:
 * direct validation against a supplied User object,
 * and authentication through UserRepository.
 *
 * Invalid or missing credentials result in a failed login.
 */
public class LoginService {

    /**
     * Repository used to retrieve stored user information.
     */
    private final UserRepository userRepository;

    /**
     * Creates a LoginService using the default
     * UserRepository implementation.
     */
    public LoginService() {
        this(new UserRepository());
    }

    /**
     * Creates a LoginService with a specified repository.
     *
     * This constructor supports dependency injection
     * and allows repositories to be supplied for testing.
     *
     * @param userRepository repository used to find users
     * @throws IllegalArgumentException if the repository is null
     */
    public LoginService(UserRepository userRepository) {

        if (userRepository == null) {
            throw new IllegalArgumentException(
                    "UserRepository cannot be null");
        }

        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user by comparing the supplied
     * credentials with a given stored User object.
     *
     * This method is retained for compatibility
     * with existing tests and functionality.
     *
     * Authentication fails if the username, password,
     * or stored user is null.
     *
     * @param username username entered by the user
     * @param password password entered by the user
     * @param storedUser stored user to authenticate against
     * @return true if both credentials match;
     *         false otherwise
     */
    public boolean login(
            String username,
            String password,
            User storedUser) {

        if (username == null || password == null
                || storedUser == null) {
            return false;
        }

        return username.equals(storedUser.getUsername())
                && password.equals(storedUser.getPassword());
    }

    /**
     * Authenticates a user using the UserRepository.
     *
     * The repository searches for a stored user
     * matching the provided username.
     *
     * The retrieved user is then validated using
     * the existing three-argument login method.
     *
     * Authentication fails if either credential
     * is null or no matching user is found.
     *
     * @param username username entered by the user
     * @param password password entered by the user
     * @return true if the credentials are valid;
     *         false otherwise
     */
    public boolean login(
            String username,
            String password) {

        if (username == null || password == null) {
            return false;
        }

        User storedUser =
                userRepository.findByUsername(username);

        return login(username, password, storedUser);
    }
}

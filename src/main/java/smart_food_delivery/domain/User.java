
package smart_food_delivery.domain;

/**
 * Represents a user in the Smart Food Delivery
 * Management System.
 *
 * Stores the user's username, password, and role.
 */
public class User {

    private String password;
    private String username;
    private String role;

    /**
     * Creates a new user with login information
     * and a specified role.
     *
     * @param username the user's username
     * @param password the user's password
     * @param role the user's role in the system
     */
    public User(String username, String password, String role) {
        this.password = password;
        this.username = username;
        this.role = role;
    }

    /**
     * Returns the user's password.
     *
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's username.
     *
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the user's role.
     *
     * @return the user's role
     */
    public String getRole() {
        return role;
    }
}

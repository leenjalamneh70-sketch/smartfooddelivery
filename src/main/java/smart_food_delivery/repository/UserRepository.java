
package smart_food_delivery.repository;

import smart_food_delivery.domain.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Provides access to stored user information in the
 * Smart Food Delivery Management System.
 *
 * This repository reads user records from the users.txt
 * resource file and searches for users by username.
 *
 * Each valid record must contain three comma-separated
 * fields: username, password, and role.
 *
 * Invalid records are skipped during the search.
 * If the requested user is not found, null is returned.
 *
 * An exception is thrown if the resource file is missing
 * or cannot be read.
 */
public class UserRepository {

    /**
     * Creates a repository for retrieving user records
     * from the users.txt resource file.
     */
    public UserRepository() {
    }

    /**
     * Searches for a user with the specified username.
     *
     * The method loads users.txt from the application
     * classpath using the class loader.
     *
     * Each line is split into three fields:
     * username, password, and role.
     *
     * Records that do not contain exactly three fields
     * are ignored.
     *
     * If a matching username is found, a new User object
     * is created using the stored credentials and role.
     *
     * @param username username to search for
     * @return the matching User object, or null if the
     *         username is null or no matching user exists
     * @throws IllegalStateException if users.txt is missing
     *         or an I/O error occurs while reading it
     */
    public User findByUsername(String username) {

        if (username == null) {
            return null;
        }

        InputStream input = getClass()
                .getClassLoader()
                .getResourceAsStream("users.txt");

        if (input == null) {
            throw new IllegalStateException(
                    "users.txt file not found");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        input, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length != 3) {
                    continue;
                }

                String storedUsername = data[0].trim();
                String storedPassword = data[1].trim();
                String role = data[2].trim();

                if (storedUsername.equals(username)) {
                    return new User(
                            storedUsername,
                            storedPassword,
                            role);
                }
            }

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Error reading users.txt", e);
        }

        return null;
    }
}

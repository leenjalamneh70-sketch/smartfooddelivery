
package smart_food_delivery.repository;

import smart_food_delivery.domain.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class UserRepository {

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

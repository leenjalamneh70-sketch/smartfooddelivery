
package smart_food_delivery.service;

import smart_food_delivery.domain.User;
import smart_food_delivery.repository.UserRepository;

public class LoginService {

    private final UserRepository userRepository;

    public LoginService() {
        this(new UserRepository());
    }

    public LoginService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException(
                    "UserRepository cannot be null");
        }
        this.userRepository = userRepository;
    }

    // Existing method - kept for previous tests
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

    // New method - checks users.txt
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

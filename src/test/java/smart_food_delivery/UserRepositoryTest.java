
package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.User;
import smart_food_delivery.repository.UserRepository;

class UserRepositoryTest {

    private final UserRepository repository =
            new UserRepository();

    @Test
    void testFindExistingUser() {

        User user = repository.findByUsername("leen");

        assertNotNull(user);
        assertEquals("leen", user.getUsername());
        assertEquals("customer", user.getRole());
    }

    @Test
    void testFindNabila() {

        User user = repository.findByUsername("nabila");

        assertNotNull(user);
        assertEquals("nabila", user.getUsername());
        assertEquals("customer", user.getRole());
    }

    @Test
    void testFindAdmin() {

        User user = repository.findByUsername("admin");

        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("manager", user.getRole());
    }

    @Test
    void testUnknownUser() {

        User user = repository.findByUsername("unknown");

        assertNull(user);
    }

    @Test
    void testNullUsername() {

        User user = repository.findByUsername(null);

        assertNull(user);
    }

    @Test
    void testCaseSensitiveUsername() {

        User user = repository.findByUsername("LEEN");

        assertNull(user);
    }
}

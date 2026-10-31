package smart_food_delivery;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import smart_food_delivery.domain.User;
import smart_food_delivery.service.LoginService;

class LoginServiceTest {

	@Test
	void testValidLogin() {
	User x = new User("leen","1234","customer");
	LoginService service =new LoginService ();
	boolean result = service.login("leen","1234",x);
	assertTrue(result);
	
	}
	@Test
	void testInvalidPassword() {
		User x = new User("leen","1234","customer");
		LoginService service =new LoginService ();
		boolean result = service.login("leen","9999",x);
		assertFalse(result);

	}
	@Test
	void testInvalidusername() {
		User x = new User("leen","1234","customer");
		LoginService service =new LoginService ();
		boolean result = service.login("lee","1234",x);
		assertFalse(result);

	}
	@Test
	void testNullUser () {
		LoginService service =new LoginService ();
		boolean result = service.login("leen","9999",null);
		assertFalse(result);
	}
	
}

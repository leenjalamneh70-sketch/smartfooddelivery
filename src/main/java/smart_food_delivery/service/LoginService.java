package smart_food_delivery.service;
import smart_food_delivery.domain.User;

public class LoginService {
	public boolean login (String username, String password ,User storedUser ) {
		if(storedUser == null) {
			return false;
		}
		    if (username.equals(storedUser.getUsername()) && password.equals(storedUser.getPassword()) ) {
			    return true;
		    }
    
		return false;
	}
}

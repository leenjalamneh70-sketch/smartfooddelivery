package smart_food_delivery.presentation;
import smart_food_delivery.domain.User;
import smart_food_delivery.service.LoginService;
import java.util.Scanner;

public class LoginUI {

	public static void main(String[] args) {
		User user=new User ("leen","1234","costumer");
		LoginService service = new LoginService();
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter username: ");
		String enteredUsername = input.nextLine();
		
		System.out.print("Enter password: ");
		String enteredPassword = input.nextLine();

boolean result = service.login(enteredUsername , enteredPassword, user );
if (result) {
    System.out.println("Login successful!");
} else {
    System.out.println("Error: Invalid username or password.");
}
	}

}

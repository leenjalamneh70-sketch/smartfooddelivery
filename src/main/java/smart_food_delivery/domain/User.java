package smart_food_delivery.domain;

public class User {
	private String password;
	private String username;
	private String role;
	
	public User(String username, String password, String role) {
		this.password = password;
		this.username = username;
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public String getUsername() {
		return username;
	}
	public String getRole() {
		return role;
	}
}

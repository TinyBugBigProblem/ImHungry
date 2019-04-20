package database;


public class forTest {

	public static void main(String argv[]) {
		
		HashPassword h = new HashPassword();
		Database d = new Database();
		
		String password = "csci310";
		
		System.out.println("Password: " + password);
		System.out.println("Hashed Password: " + h.getHashPassword(password));
		
		System.out.println("Sign Up User: " + d.signUpUser("Vincents", password));
		System.out.println("Sign In User: " + d.signInUser("Vincent", password));
		
	}
	
}

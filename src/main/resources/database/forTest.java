package database;

public class forTest {

	public static void main(String argv[]) {
		
		HashPassword h = new HashPassword();
		
		String password = "csci310";
		
		System.out.println("Password: " + password);
		System.out.println("Hashed Password: " + h.getHashPassword(password));
		
	}
	
}

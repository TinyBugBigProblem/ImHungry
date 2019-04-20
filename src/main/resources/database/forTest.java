package database;


public class forTest {

	public static void main(String argv[]) {
		
		HashPassword h = new HashPassword();
		Database d = new Database();
		
		String password = "csci310";
		
		System.out.println("Password: " + password);
		System.out.println("Hashed Password: " + h.getHashPassword(password));
		
		System.out.println("Sign Up User: \n" + d.signUpUser("Vincents", password));
		System.out.println("Sign In User: \n" + d.signInUser("Vincent", password));
		
		System.out.println("Add Grocery: \n" + d.addGroceryItem("Salt", 1.1, "spoon", "What is this", 2.2, "Vincents"));
		System.out.println("Remove Grocery: \n" + d.removeGroceryItem("Saslt", 1.1, "spoon", "What is this", 2.2, "Vincents"));
	}
	
}

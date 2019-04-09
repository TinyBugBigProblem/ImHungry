package database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import data.GroceryList;
import data.ListItem;

/*
 * Database class
 */
public class Database {
	/*
	 * Private member variables
	 */
	private FileInputStream serviceAccount;
	private FirebaseOptions options;
	private FirebaseDatabase ref;
	
	/*
	 * Initialize Database
	 */
	public Database() {
		try {
			serviceAccount = new FileInputStream("project2-privatekey.json");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://project2-46020.firebaseio.com")
					.build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Initialize app with service account and admin priveleges
		FirebaseApp.initializeApp(options);
		// Access to all read and write priveledges
		ref = FirebaseDatabase.getInstance();
	}
	/*
	 * Add recipe to database
	 */
	public boolean add() {
		DatabaseReference database = ref.getReference("/project2-46020/recipe");
		return true;
	}
	/*
	 * Add user to database
	 */
	public boolean addUser() {
		return true;
	}
	/*
	 * Edit users data
	 */
	public boolean editUser() {
		return true;
	}
	/*
	 * Add recipe to user
	 */
	public boolean addRecipe() {
		return true;
	}
	/*
	 * Add restaurant to user
	 */
	public boolean addRestaurant() {
		return true;
	}
	/*
	 * Add grocery item to user
	 */
	public boolean addGrocery(){
		return true;
	}
	/*
	 * Get users recipe, restaurant list
	 */
	public ListItem getUserList() {
		ListItem temp = null;
		return temp;
	}
	/*
	 * Get users grocery list
	 */
	public GroceryList getUserGrocery() {
		GroceryList temp = null;
		return temp;
	}
}

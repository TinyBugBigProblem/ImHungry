package databasetests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.GroceryItem;
import data.GroceryList;
import data.Recipe;
import data.Restaurant;
import data.UserInfo;
import data.UserList;
import database.Database;
import database.HashPassword;

/*
 * Tests for UserInfo class.
 */
public class DatabaseTest {
	
	private Connection conn;
	private Statement st;
	private ResultSet rs;
	private PreparedStatement ps;
	
	private Database database;
	private UserInfo signupUser;
	private UserInfo user;
	private UserList userlist;
	
	private GroceryList grocerylist;
	
	@Before
	public void setup() throws Exception {
		
		conn = null;
		st = null;
		rs = null;
		ps = null;
		
		database = new Database();
		signupUser = new UserInfo("Vincent", "Csci310");
		user = new UserInfo("Liang", "USC");
		userlist = new UserList();
		grocerylist = new GroceryList();
		
		database.signUpUser(user);
		
		GroceryItem groceryitem1 = new GroceryItem("Salt", 1, "spoon", "1", 1);
		GroceryItem groceryitem2 = new GroceryItem("Sugar", 2, "cup", "2", 10);
		GroceryItem groceryitem3 = new GroceryItem("Toast", 3, "piece", "3", 100);
		GroceryItem groceryitem4 = new GroceryItem("Steak", 4, "oz", "4", 1000);

		grocerylist.add(groceryitem1);
		grocerylist.add(groceryitem2);
		grocerylist.add(groceryitem3);
		grocerylist.add(groceryitem4);
		
		Restaurant usc = new Restaurant("USC", "usc.edu", 5, "USC Campus", "123456789", 5.0, 3);
		Restaurant ucla = new Restaurant("UCLA", "ucla.edu", 2, "UCLA Campus", "987654321", 0.1, 10);
		Restaurant stanford = new Restaurant("Stanford", "stanford.edu", 5, "Stanford Campus", "456789123", 5.0, 20);
		Restaurant berkeley = new Restaurant("Berkeley", "berkeley.edu", 2, "Berkeley Campus", "789123456", 5.0, 30);
		
		String name = "Good Food";
		String pictureUrl = "http://www.todayifoundout.com/wp-content/uploads/2017/11/rick-astley.png";
		double prepTime = 10;
		double cookTime = 25;
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("1 teaspoon ground ginger");
		ingredients.add("1 rack of lamb");
		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("Throw in a pan.");
		instructions.add("Cook until done.");
		double rating = 4.5;
		
		Recipe recipe1 = new Recipe(name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);
		Recipe recipe2 = new Recipe(name, pictureUrl, prepTime - 3, cookTime + 2, ingredients, instructions, rating - 1);
		Recipe recipe3 = new Recipe(name, pictureUrl, prepTime - 5, cookTime + 6, ingredients, instructions, rating - 2);
		
		userlist.add(usc);
		userlist.add(ucla);
		userlist.add(stanford);
		userlist.add(berkeley);
		userlist.add(recipe1);
		userlist.add(recipe2);
		userlist.add(recipe3);
		
	}
	
	@Test
	public void testSignUp() throws SQLException {
		
		String[] str = new String[2];
		str = database.signUpUser(signupUser);
		assertEquals("true", str[0]);
		
		// If Signing Up by the same username and password
		str = database.signUpUser(signupUser);
		assertEquals("false", str[0]);
		
		UserInfo user2 = new UserInfo("Vincent", "csci310");
		str = database.signUpUser(user2);
		assertEquals("false", str[0]);

		String username = "";
		String password = "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Info?user=root&password=root&useSSL=false");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		}
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Userinfo WHERE Username = '" + signupUser.getUsername() + "';");
			while (rs.next()) {
				username = rs.getString("Username");
				password = rs.getString("UserPassword");				
				return;
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		// Verify if username and password are in database
		HashPassword hash = new HashPassword();
		assertEquals(username, signupUser.getUsername());
		assertEquals(password, hash.getHashPassword(signupUser.getPassword()));
		
	}

	@Test
	public void testSignIn() {
		
		String[] str = new String[2];
		str = database.signInUser(user);
		assertEquals("true", str[0]);
		
		//wrong username
		UserInfo user2 = new UserInfo("Liamg", "USC");
		str = database.signInUser(user2);
		assertEquals("false", str[0]);
		
		//wrong password
		UserInfo user3 = new UserInfo("Liang", "UCLA");
		str = database.signInUser(user3);
		assertEquals("false", str[0]);
		
		UserInfo user4 = new UserInfo("noThisUser", "NothisUser");
		str = database.signInUser(user4);
		assertEquals("false", str[0]);
	
	}
	
	@Test
	public void testAddGroceryList() throws SQLException {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Info?user=root&password=root&useSSL=false");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		}
		
		String[] str = new String[2];		
		GroceryItem groceryitem = new GroceryItem("test", 1, "spoon", "1", 1);
		str = database.addGroceryItem(groceryitem, user);
		assertEquals("true", str[0]);
		
		String name = "";
		double amount = 0.0;
		String units = "";
		String qualifier = "";
		double cost = 0.0;
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM GroceryList WHERE GroceryName = 'test';");
			while (rs.next()) {
				name = rs.getString("GroceryName");
				amount = rs.getDouble("Amount");
				units = rs.getString("Units");
				qualifier = rs.getString("Qualifier");
				cost = rs.getDouble("Cost");
				
				return;
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		
		assertEquals(name, groceryitem.getName());
		assertTrue(amount == groceryitem.getAmount());
		assertEquals(units, groceryitem.getUnits());
		assertEquals(qualifier, groceryitem.getQualifier());
		assertTrue(cost == groceryitem.getCost());
		
		// test store the duplicated item
		str = database.addGroceryItem(groceryitem, user);
		assertEquals("false", str[0]);
		
	}
	
	@Test
	public void testRemoveGroceryList() throws SQLException {
		
		String[] str = new String[2];	
		
		// remove an item which does not exist
		GroceryItem groceryitemno = new GroceryItem("no", 1, "no", "no", 1);
		str = database.removeGroceryItem(groceryitemno, user);
		assertEquals("false", str[0]);
		
		GroceryItem groceryitem = new GroceryItem("test", 1, "spoon", "1", 1);
		str = database.removeGroceryItem(groceryitem, user);
		assertEquals("true", str[0]);
		
		// remove twice
		str = database.removeGroceryItem(groceryitem, user);
		assertEquals("false", str[0]);
		
	}
	
	@Test
	public void testGetGroceryList() throws SQLException {
		
		database.addGroceryItem(this.grocerylist.getGroceries().get(0), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(1), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(2), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(3), user);
		
		GroceryList grocerylist = database.getGroceryList(user);
		
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(0)));
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(1)));
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(2)));
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(3)));
		
		database.removeGroceryItem(this.grocerylist.getGroceries().get(2), user);
		grocerylist = database.getGroceryList(user);
		
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(0)));
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(1)));
		assertFalse(grocerylist.contains(this.grocerylist.getGroceries().get(2)));
		assertTrue(grocerylist.contains(this.grocerylist.getGroceries().get(3)));
		
		database.removeGroceryItem(this.grocerylist.getGroceries().get(0), user);
		database.removeGroceryItem(this.grocerylist.getGroceries().get(1), user);
		database.removeGroceryItem(this.grocerylist.getGroceries().get(3), user);
		grocerylist = database.getGroceryList(user);
		
		assertFalse(grocerylist.contains(this.grocerylist.getGroceries().get(0)));
		assertFalse(grocerylist.contains(this.grocerylist.getGroceries().get(1)));
		assertFalse(grocerylist.contains(this.grocerylist.getGroceries().get(2)));
		assertFalse(grocerylist.contains(this.grocerylist.getGroceries().get(3)));
		
	}
	
	@Test
	public void testReorderGroceryList() throws SQLException {
		
		database.addGroceryItem(this.grocerylist.getGroceries().get(0), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(1), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(2), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(3), user);
		
		GroceryList grocerylist = database.getGroceryList(user);
	
		assertEquals(grocerylist.getGroceries().get(0), this.grocerylist.getGroceries().get(0));
		
	}
	
}

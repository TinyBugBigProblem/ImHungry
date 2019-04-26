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
import java.util.StringTokenizer;

import org.junit.Before;
import org.junit.Test;

import data.GroceryItem;
import data.GroceryList;
import data.ListItem;
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
		
		database.removeGroceryItem(groceryitem, user);
		
	}
	
	@Test
	public void testRemoveGroceryList() throws SQLException {
		
		String[] str = new String[2];	
		
		// remove an item which does not exist
		GroceryItem groceryitemno = new GroceryItem("no", 1, "no", "no", 1);
		
		str = database.removeGroceryItem(groceryitemno, user);
		assertEquals("false", str[0]);
		
		GroceryItem groceryitem = new GroceryItem("test", 1, "spoon", "1", 1);
		str = database.addGroceryItem(groceryitem, user);
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
		
		// clean GroceryList in database
		GroceryList nothing = new GroceryList();
		database.updateGroceryListOrder(nothing, user);
		
		database.addGroceryItem(this.grocerylist.getGroceries().get(0), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(1), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(2), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(3), user);
		
		GroceryList grocerylist = database.getGroceryList(user);
	
		assertEquals(grocerylist.getGroceries().get(0), this.grocerylist.getGroceries().get(0));
		assertEquals(grocerylist.getGroceries().get(1), this.grocerylist.getGroceries().get(1));
		assertEquals(grocerylist.getGroceries().get(2), this.grocerylist.getGroceries().get(2));
		assertEquals(grocerylist.getGroceries().get(3), this.grocerylist.getGroceries().get(3));
	
		database.updateGroceryListOrder(nothing, user);
		
		database.addGroceryItem(this.grocerylist.getGroceries().get(0), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(2), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(3), user);
		database.addGroceryItem(this.grocerylist.getGroceries().get(1), user);
		
		grocerylist = database.getGroceryList(user);
		
		assertEquals(grocerylist.getGroceries().get(0), this.grocerylist.getGroceries().get(0));
		assertEquals(grocerylist.getGroceries().get(1), this.grocerylist.getGroceries().get(2));
		assertEquals(grocerylist.getGroceries().get(2), this.grocerylist.getGroceries().get(3));
		assertEquals(grocerylist.getGroceries().get(3), this.grocerylist.getGroceries().get(1));
	
	}
	
	@Test
	public void testAddFavoriteListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateFavoriteListOrder(nothing, user);
		
		// restaurant
		database.addFavoriteListItem(userlist.getLists().get(0), user);
		// recipe
		database.addFavoriteListItem(userlist.getLists().get(4), user);
		
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
			rs = st.executeQuery("SELECT * FROM FavoriteRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("FRName");
				String url = rs.getString("FRUrl");
				int price = rs.getInt("FRPrice");
				String address = rs.getString("FRAddress");
				String phonenumner = rs.getString("FRPhoneNumber");
				double rating = rs.getDouble("FRRating");
				int drivingTime = rs.getInt("FRDrivingTime");
				
				assertEquals(name, userlist.getRestaurants().get(0).getName());
				assertEquals(url, userlist.getRestaurants().get(0).getWebsiteUrl());
				assertEquals(price, userlist.getRestaurants().get(0).getPrice());
				assertEquals(address, userlist.getRestaurants().get(0).getAddress());
				assertEquals(phonenumner, userlist.getRestaurants().get(0).getPhoneNumber());
				assertTrue(rating == userlist.getRestaurants().get(0).getRating());
				assertEquals(drivingTime, userlist.getRestaurants().get(0).getDrivingTime());
			
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
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM FavoriteRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("FPName");
				String url = rs.getString("FPUrl");
				double prepareTime = rs.getDouble("FPPrepareTime");
				double cookingTime = rs.getDouble("FPCookingTime");
				
				ArrayList<String> ingredients = new ArrayList<String>();
				String ingredient = rs.getString("FPIngredients");
				StringTokenizer tokenizerit = new StringTokenizer(ingredient, ",");
				while(tokenizerit.hasMoreTokens()) {
					ingredients.add(tokenizerit.nextToken());
				}
				
				ArrayList<String> instructions = new ArrayList<String>();
				String instruction = rs.getString("FPInstructions");
				StringTokenizer tokenizerin = new StringTokenizer(instruction, ",");
				while(tokenizerin.hasMoreTokens()) {
					instructions.add(tokenizerin.nextToken());
				}
				
				double rating = rs.getDouble("FPRating");
				
				assertEquals(name, userlist.getRecipes().get(0).getName());
				assertEquals(url, userlist.getRecipes().get(0).getPictureUrl());
				assertTrue(prepareTime == userlist.getRecipes().get(0).getPrepTime());
				assertTrue(cookingTime == userlist.getRecipes().get(0).getCookTime());
				assertEquals(ingredients, userlist.getRecipes().get(0).getIngredients());
				assertEquals(instructions, userlist.getRecipes().get(0).getInstructions());
				assertTrue(rating == userlist.getRecipes().get(0).getRating());
	
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
		
	}
	
	@Test
	public void testRemoveFavoriteListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateFavoriteListOrder(nothing, user);
		
		String[] str = new String[2];
		
		// restaurant
		database.addFavoriteListItem(userlist.getLists().get(0), user);
		// recipe
		database.addFavoriteListItem(userlist.getLists().get(4), user);
		
		
		//remove
		str = database.removeFavoriteListItem(userlist.getLists().get(0), user);
		assertEquals("true", str[0]);
		str = database.removeFavoriteListItem(userlist.getLists().get(4), user);
		assertEquals("true", str[0]);
		
		boolean findExisted = false;
		
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
			rs = st.executeQuery("SELECT * FROM FavoriteRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
			
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
		
		assertFalse(findExisted);
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM FavoriteRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
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

		assertFalse(findExisted);

		// remove twice
		str = database.removeFavoriteListItem(userlist.getLists().get(0), user);
		assertEquals("false", str[0]);
		str = database.removeFavoriteListItem(userlist.getLists().get(4), user);
		assertEquals("false", str[0]);

	}
	
	@Test
	public void testGetFavoriteListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateFavoriteListOrder(nothing, user);
		
		// restaurant
		database.addFavoriteListItem(userlist.getLists().get(0), user);
		database.addFavoriteListItem(userlist.getLists().get(1), user);
		database.addFavoriteListItem(userlist.getLists().get(2), user);
		// recipe
		database.addFavoriteListItem(userlist.getLists().get(4), user);
		
		UserList getlist = database.getFavoriteUserList(user);
		
		assertTrue(getlist.contains(userlist.getRestaurants().get(0)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(1)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(2)));
		assertFalse(getlist.contains(userlist.getRestaurants().get(3)));
		
		assertTrue(getlist.contains(userlist.getRecipes().get(0)));
		
	}
	
	@Test
	public void testReorderFavoriteListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateFavoriteListOrder(nothing, user);
		
		// restaurant
		database.addFavoriteListItem(userlist.getLists().get(0), user);
		database.addFavoriteListItem(userlist.getLists().get(1), user);
		database.addFavoriteListItem(userlist.getLists().get(2), user);
		database.addFavoriteListItem(userlist.getLists().get(3), user);
		
		UserList getlist = database.getFavoriteUserList(user);

		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(0));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(1));
		assertEquals(getlist.getRestaurants().get(2), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(3), userlist.getRestaurants().get(3));
		
		ArrayList<ListItem> neworder = new ArrayList<ListItem>();
		neworder.add(userlist.getLists().get(2));
		neworder.add(userlist.getLists().get(3));
		neworder.add(userlist.getLists().get(4));
		neworder.add(userlist.getLists().get(1));
		
		database.updateFavoriteListOrder(neworder, user);
		getlist = database.getFavoriteUserList(user);
		
		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(3));
		
	}
	
	@Test
	public void testAddDoNotShowListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateDoNotShowListOrder(nothing, user);
		
		// restaurant
		database.addDoNotShowListItem(userlist.getLists().get(0), user);
		// recipe
		database.addDoNotShowListItem(userlist.getLists().get(4), user);
		
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
			rs = st.executeQuery("SELECT * FROM DoNotShowRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("DNSRName");
				String url = rs.getString("DNSRUrl");
				int price = rs.getInt("DNSRPrice");
				String address = rs.getString("DNSRAddress");
				String phonenumner = rs.getString("DNSRPhoneNumber");
				double rating = rs.getDouble("DNSRRating");
				int drivingTime = rs.getInt("DNSRDrivingTime");
				
				assertEquals(name, userlist.getRestaurants().get(0).getName());
				assertEquals(url, userlist.getRestaurants().get(0).getWebsiteUrl());
				assertEquals(price, userlist.getRestaurants().get(0).getPrice());
				assertEquals(address, userlist.getRestaurants().get(0).getAddress());
				assertEquals(phonenumner, userlist.getRestaurants().get(0).getPhoneNumber());
				assertTrue(rating == userlist.getRestaurants().get(0).getRating());
				assertEquals(drivingTime, userlist.getRestaurants().get(0).getDrivingTime());
			
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
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM DoNotShowRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("DNSPName");
				String url = rs.getString("DNSPUrl");
				double prepareTime = rs.getDouble("DNSPPrepareTime");
				double cookingTime = rs.getDouble("DNSPCookingTime");
				
				ArrayList<String> ingredients = new ArrayList<String>();
				String ingredient = rs.getString("DNSPIngredients");
				StringTokenizer tokenizerit = new StringTokenizer(ingredient, ",");
				while(tokenizerit.hasMoreTokens()) {
					ingredients.add(tokenizerit.nextToken());
				}
				
				ArrayList<String> instructions = new ArrayList<String>();
				String instruction = rs.getString("DNSPInstructions");
				StringTokenizer tokenizerin = new StringTokenizer(instruction, ",");
				while(tokenizerin.hasMoreTokens()) {
					instructions.add(tokenizerin.nextToken());
				}
				
				double rating = rs.getDouble("DNSPRating");
				
				assertEquals(name, userlist.getRecipes().get(0).getName());
				assertEquals(url, userlist.getRecipes().get(0).getPictureUrl());
				assertTrue(prepareTime == userlist.getRecipes().get(0).getPrepTime());
				assertTrue(cookingTime == userlist.getRecipes().get(0).getCookTime());
				assertEquals(ingredients, userlist.getRecipes().get(0).getIngredients());
				assertEquals(instructions, userlist.getRecipes().get(0).getInstructions());
				assertTrue(rating == userlist.getRecipes().get(0).getRating());
	
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
		
	}
	
	@Test
	public void testRemoveDoNotShowListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateDoNotShowListOrder(nothing, user);
		
		String[] str = new String[2];
		
		// restaurant
		database.addDoNotShowListItem(userlist.getLists().get(0), user);
		// recipe
		database.addDoNotShowListItem(userlist.getLists().get(4), user);
		
		
		//remove
		str = database.removeDoNotShowListItem(userlist.getLists().get(0), user);
		assertEquals("true", str[0]);
		str = database.removeDoNotShowListItem(userlist.getLists().get(4), user);
		assertEquals("true", str[0]);
		
		boolean findExisted = false;
		
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
			rs = st.executeQuery("SELECT * FROM DoNotShowRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
			
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
		
		assertFalse(findExisted);
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM DoNotShowRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
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

		assertFalse(findExisted);

		// remove twice
		str = database.removeDoNotShowListItem(userlist.getLists().get(0), user);
		assertEquals("false", str[0]);
		str = database.removeDoNotShowListItem(userlist.getLists().get(4), user);
		assertEquals("false", str[0]);

	}
	
	@Test
	public void testGetDoNotShowListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateDoNotShowListOrder(nothing, user);
		
		// restaurant
		database.addDoNotShowListItem(userlist.getLists().get(0), user);
		database.addDoNotShowListItem(userlist.getLists().get(1), user);
		database.addDoNotShowListItem(userlist.getLists().get(2), user);
		// recipe
		database.addDoNotShowListItem(userlist.getLists().get(4), user);
		
		UserList getlist = database.getDoNotShowUserList(user);
		
		assertTrue(getlist.contains(userlist.getRestaurants().get(0)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(1)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(2)));
		assertFalse(getlist.contains(userlist.getRestaurants().get(3)));
		
		assertTrue(getlist.contains(userlist.getRecipes().get(0)));
		
	}
	
	@Test
	public void testReorderDoNotShowListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateDoNotShowListOrder(nothing, user);
		
		// restaurant
		database.addDoNotShowListItem(userlist.getLists().get(0), user);
		database.addDoNotShowListItem(userlist.getLists().get(1), user);
		database.addDoNotShowListItem(userlist.getLists().get(2), user);
		database.addDoNotShowListItem(userlist.getLists().get(3), user);
		
		UserList getlist = database.getDoNotShowUserList(user);

		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(0));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(1));
		assertEquals(getlist.getRestaurants().get(2), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(3), userlist.getRestaurants().get(3));
		
		ArrayList<ListItem> neworder = new ArrayList<ListItem>();
		neworder.add(userlist.getLists().get(2));
		neworder.add(userlist.getLists().get(3));
		neworder.add(userlist.getLists().get(4));
		neworder.add(userlist.getLists().get(1));
		
		database.updateDoNotShowListOrder(neworder, user);
		getlist = database.getDoNotShowUserList(user);
		
		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(3));
		
	}
	
	@Test
	public void testAddToEcploreListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateToExploreListOrder(nothing, user);
		
		// restaurant
		database.addToExploreListItem(userlist.getLists().get(0), user);
		// recipe
		database.addToExploreListItem(userlist.getLists().get(4), user);
		
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
			rs = st.executeQuery("SELECT * FROM ToExploreRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("TERName");
				String url = rs.getString("TERUrl");
				int price = rs.getInt("TERPrice");
				String address = rs.getString("TERAddress");
				String phonenumner = rs.getString("TERPhoneNumber");
				double rating = rs.getDouble("TERRating");
				int drivingTime = rs.getInt("TERDrivingTime");
				
				assertEquals(name, userlist.getRestaurants().get(0).getName());
				assertEquals(url, userlist.getRestaurants().get(0).getWebsiteUrl());
				assertEquals(price, userlist.getRestaurants().get(0).getPrice());
				assertEquals(address, userlist.getRestaurants().get(0).getAddress());
				assertEquals(phonenumner, userlist.getRestaurants().get(0).getPhoneNumber());
				assertTrue(rating == userlist.getRestaurants().get(0).getRating());
				assertEquals(drivingTime, userlist.getRestaurants().get(0).getDrivingTime());
			
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
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM ToExploreRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("TEPName");
				String url = rs.getString("TEPUrl");
				double prepareTime = rs.getDouble("TEPPrepareTime");
				double cookingTime = rs.getDouble("TEPCookingTime");
				
				ArrayList<String> ingredients = new ArrayList<String>();
				String ingredient = rs.getString("TEPIngredients");
				StringTokenizer tokenizerit = new StringTokenizer(ingredient, ",");
				while(tokenizerit.hasMoreTokens()) {
					ingredients.add(tokenizerit.nextToken());
				}
				
				ArrayList<String> instructions = new ArrayList<String>();
				String instruction = rs.getString("TEPInstructions");
				StringTokenizer tokenizerin = new StringTokenizer(instruction, ",");
				while(tokenizerin.hasMoreTokens()) {
					instructions.add(tokenizerin.nextToken());
				}
				
				double rating = rs.getDouble("TEPRating");
				
				assertEquals(name, userlist.getRecipes().get(0).getName());
				assertEquals(url, userlist.getRecipes().get(0).getPictureUrl());
				assertTrue(prepareTime == userlist.getRecipes().get(0).getPrepTime());
				assertTrue(cookingTime == userlist.getRecipes().get(0).getCookTime());
				assertEquals(ingredients, userlist.getRecipes().get(0).getIngredients());
				assertEquals(instructions, userlist.getRecipes().get(0).getInstructions());
				assertTrue(rating == userlist.getRecipes().get(0).getRating());
	
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
		
	}
	
	@Test
	public void testRemoveToExploreListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateToExploreListOrder(nothing, user);
		
		String[] str = new String[2];
		
		// restaurant
		database.addToExploreListItem(userlist.getLists().get(0), user);
		// recipe
		database.addToExploreListItem(userlist.getLists().get(4), user);
		
		
		//remove
		str = database.removeToExploreListItem(userlist.getLists().get(0), user);
		assertEquals("true", str[0]);
		str = database.removeToExploreListItem(userlist.getLists().get(4), user);
		assertEquals("true", str[0]);
		
		boolean findExisted = false;
		
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
			rs = st.executeQuery("SELECT * FROM ToExploreRestaurant WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
			
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
		
		assertFalse(findExisted);
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM ToExploreRecipe WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				findExisted = true;
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

		assertFalse(findExisted);

		// remove twice
		str = database.removeToExploreListItem(userlist.getLists().get(0), user);
		assertEquals("false", str[0]);
		str = database.removeToExploreListItem(userlist.getLists().get(4), user);
		assertEquals("false", str[0]);

	}
	
	@Test
	public void testGetToExploreListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateToExploreListOrder(nothing, user);
		
		// restaurant
		database.addToExploreListItem(userlist.getLists().get(0), user);
		database.addToExploreListItem(userlist.getLists().get(1), user);
		database.addToExploreListItem(userlist.getLists().get(2), user);
		// recipe
		database.addToExploreListItem(userlist.getLists().get(4), user);
		
		UserList getlist = database.getToExploreUserList(user);
		
		assertTrue(getlist.contains(userlist.getRestaurants().get(0)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(1)));
		assertTrue(getlist.contains(userlist.getRestaurants().get(2)));
		assertFalse(getlist.contains(userlist.getRestaurants().get(3)));
		
		assertTrue(getlist.contains(userlist.getRecipes().get(0)));
		
	}
	
	@Test
	public void testReorderToExploreListItem() throws SQLException {
		
		ArrayList<ListItem> nothing = new ArrayList<ListItem>();
		database.updateToExploreListOrder(nothing, user);
		
		// restaurant
		database.addToExploreListItem(userlist.getLists().get(0), user);
		database.addToExploreListItem(userlist.getLists().get(1), user);
		database.addToExploreListItem(userlist.getLists().get(2), user);
		database.addToExploreListItem(userlist.getLists().get(3), user);
		
		UserList getlist = database.getToExploreUserList(user);

		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(0));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(1));
		assertEquals(getlist.getRestaurants().get(2), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(3), userlist.getRestaurants().get(3));
		
		ArrayList<ListItem> neworder = new ArrayList<ListItem>();
		neworder.add(userlist.getLists().get(2));
		neworder.add(userlist.getLists().get(3));
		neworder.add(userlist.getLists().get(4));
		neworder.add(userlist.getLists().get(1));
		
		database.updateToExploreListOrder(neworder, user);
		getlist = database.getToExploreUserList(user);
		
		assertEquals(getlist.getRestaurants().get(0), userlist.getRestaurants().get(2));
		assertEquals(getlist.getRestaurants().get(1), userlist.getRestaurants().get(3));
		
	}

}

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;

import data.Recipe;
import data.Restaurant;
import data.UserInfo;

public class Database {
	
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	public Database() {
		
	}
	
	private void connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Info?user=root&password=root&useSSL=false");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		}
	}
	
	private boolean findExistedUser(String username) { // check if username exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Userinfo WHERE Username='" + username + "';");
			while (rs.next()) {
				String uname = rs.getString("Username");
				if (uname.equals(username))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Sign-In User Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	/* Handle User Information */
	public String[] signUpUser(String username, String password) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedUser(username)) {
			try {
				ps = conn.prepareStatement("insert into Userinfo(Username, UserPassword) VALUES(?, ?)");
				ps.setString(1, username);
				// hash it
				HashPassword hash = new HashPassword();
				String hashedpasswrod = hash.getHashPassword(password);
				ps.setString(2, hashedpasswrod);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				System.out.println("Sign-Up User Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("User: " + username + " SignUp Successfully.");
			status = "true";
			comment = "User: " + username + " SignUp Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Username has been used.");
		status =  "false";
		comment = "Username: " + username + " has been used.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	public String[] signInUser(String username, String password) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM Userinfo WHERE Username='" + username + "';");
			// hash it
			HashPassword hash = new HashPassword();
			String hashedpasswrod = hash.getHashPassword(password);
			while (rs.next()) {
				ResultSet rs2 = st.executeQuery("SELECT * FROM Userinfo WHERE Username='" + username
						+ "' AND UserPassword='" + hashedpasswrod + "';");
				while (rs2.next()) {
					System.out.println("User: " + username + " Login Successfully.");
					status = "true";
					comment = "User: " + username + " Login Successfully.";
					response[0] = status;
					response[1] = comment;
					return response;
				}
				System.out.println("Password is Incorrect.");
				status = "false";
				comment = "Password is Incorrect.";
				response[0] = status;
				response[1] = comment;
				return response;
			}
		} catch (SQLException sqle) {
			System.out.println("Sign-In User Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		System.out.println("Username does not exist.");
		status =  "false";
		comment = "Username: " + username + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
//	private JSONObject getJsonObject(boolean status, String comment) {
//		JSONObject obj = new JSONObject();
//		obj.put("Status", status); // return boolean
//		obj.put("Comment", comment); // return comment
//		
//		return obj;
//	}
	
	/* Handle Grocery Information */
	private boolean findExistedGroceryItem(String groceryname) { // check if groceryname exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM GroceryList WHERE GroceryName='" + groceryname + "';");
			while (rs.next()) {
				String gname = rs.getString("GroceryName");
				if (gname.equals(groceryname))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed Grocery Item Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	public String[] addGroceryItem(String groceryname, double amount, String units, String qualifier, double cost, String username) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedGroceryItem(groceryname)) {
			try {
				ps = conn.prepareStatement(
						"insert into GroceryList(GroceryName, Amount, Units, Qualifier, Cost, Username) VALUES(?, ?, ?, ?, ?, ?)");

				ps.setString(1, groceryname);
				ps.setDouble(2, amount);
				ps.setString(3, units);
				ps.setString(4, qualifier);
				ps.setDouble(5, cost);
				ps.setString(6, username);
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Add Grocery Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Grocery: " + groceryname + " is added.");
			status =  "true";
			comment = "Grocery: " + groceryname + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Grocery is existed.");
		status =  "false";
		comment = "Grocery: " + groceryname + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	public String[] removeGroceryItem(String groceryname, double amount, String units, String qualifier, double cost, String username) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedGroceryItem(groceryname)) {
			try {
				String cmd = "DELETE FROM GroceryList WHERE GroceryName = ? AND Amount = ? AND Units = ? AND Qualifier = ? AND Cost = ? AND Username = ?";
				ps = conn.prepareStatement(cmd);
				ps.setString(1, groceryname);
				ps.setDouble(2, amount);
				ps.setString(3, units);
				ps.setString(4, qualifier);
				ps.setDouble(5, cost);
				ps.setString(6, username);
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Remove GroceryItem Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove GroceryItem: " + groceryname + " Successfully.");
			status = "true";
			comment = "Remove GroceryItem: " + groceryname + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("GroceryItem: " + groceryname + " does not exist.");
		status = "false";
		comment = "GroceryItem: " + groceryname + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	/* Handle Favorite Restaurant */
	private boolean findExistedFavoriteRestaurant(Restaurant restaurant) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM FavoriteRestaurant WHERE FRName='" + restaurant.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("FRName");
				if (gname.equals(restaurant.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed Favorite Restaurant Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	public String[] addFavoriteRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedFavoriteRestaurant(restaurant)) {
			try {
				ps = conn.prepareStatement(
						"insert into FavoriteRestaurant(FRName, FRUrl, FRPrice, FRAddress, FRPhoneNumber, FRRating, FRDrivingTime, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

				ps.setString(1, restaurant.getName());
				ps.setString(2, restaurant.getWebsiteUrl());
				ps.setInt(3, restaurant.getPrice());
				ps.setString(4, restaurant.getAddress());
				ps.setString(5, restaurant.getPhoneNumber());
				ps.setDouble(6, restaurant.getRating());
				ps.setInt(7, restaurant.getDrivingTime());
				ps.setString(8, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Add Favorite Restaurant Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Favorite Restaurant: " + restaurant.getName() + " is added.");
			status =  "true";
			comment = "Favorite Restaurant: " + restaurant.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Favorite Restaurant is existed.");
		status =  "false";
		comment = "Favorite Restauran: " + restaurant.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	public String[] removeFavoriteRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedFavoriteRestaurant(restaurant)) {
			try {
				String cmd = "DELETE FROM FavoriteRestaurant WHERE FRName = ? AND FRUrl = ? AND FRPrice = ? AND FRAddress = ? AND FRPhoneNumber = ? AND FRRating = ? AND FRDrivingTime = ? AND Username = ?";
				ps = conn.prepareStatement(cmd);
				ps.setString(1, restaurant.getName());
				ps.setString(2, restaurant.getWebsiteUrl());
				ps.setInt(3, restaurant.getPrice());
				ps.setString(4, restaurant.getAddress());
				ps.setString(5, restaurant.getPhoneNumber());
				ps.setDouble(6, restaurant.getRating());
				ps.setInt(7, restaurant.getDrivingTime());
				ps.setString(8, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Remove Favorite Restaurant Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove Favorite Restaurant: " + restaurant.getName() + " Successfully.");
			status = "true";
			comment = "Remove Favorite Restaurant: " + restaurant.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Favorite Restaurant: " + restaurant.getName() + " does not exist.");
		status = "false";
		comment = "Favorite Restaurant: " + restaurant.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	/* Handle Favorite Recipe */
	private boolean findExistedFavoriteRecipe(Recipe recipe) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM FavoriteRecipe WHERE FPName='" + recipe.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("FPName");
				if (gname.equals(recipe.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed Favorite Recipe Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	public String[] addFavoriteRecipe(Recipe recipe, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedFavoriteRecipe(recipe)) {
			try {
				ps = conn.prepareStatement(
						"insert into FavoriteRecipe(FPName, FPUrl, FPPrepareTime, FPCookingTime, FPIngredients, FPInstructions, FPRating, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

				ps.setString(1, recipe.getName());
				ps.setString(2, recipe.getPictureUrl());
				ps.setDouble(3, recipe.getPrepTime());
				ps.setDouble(4, recipe.getCookTime());
				String ingredients = "";
				for(String ingredient: recipe.getIngredients()) {
					ingredients += (ingredients == "" ? ingredient : ("," + ingredient));
				}
				ps.setString(5, ingredients);
				String instructions = "";
				for(String instruction: recipe.getInstructions()) {
					instructions += (instructions == "" ? instruction : ("," + instruction));
				}
				ps.setString(6, instructions);
				ps.setDouble(7, recipe.getRating());
				ps.setString(8, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Add Favorite Recipe Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Favorite Recipe: " + recipe.getName() + " is added.");
			status =  "true";
			comment = "Favorite Recipe: " + recipe.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Favorite Recipe is existed.");
		status =  "false";
		comment = "Favorite Recipe: " + recipe.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	public String[] removeFavoriteRecipe(Recipe recipe, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedFavoriteRecipe(recipe)) {
			try {
				String cmd = "DELETE FROM FavoriteRecipe WHERE FPName = ? AND FPUrl = ? AND FPPrepareTime = ? AND FPCookingTime = ? AND FPIngredients = ? AND FPInstructions = ? AND FPRating = ? AND Username = ?";
				ps = conn.prepareStatement(cmd);
				
				ps.setString(1, recipe.getName());
				ps.setString(2, recipe.getPictureUrl());
				ps.setDouble(3, recipe.getPrepTime());
				ps.setDouble(4, recipe.getCookTime());
				String ingredients = "";
				for(String ingredient: recipe.getIngredients()) {
					ingredients += (ingredients == "" ? ingredient : ("," + ingredient));
				}
				ps.setString(5, ingredients);
				String instructions = "";
				for(String instruction: recipe.getInstructions()) {
					instructions += (instructions == "" ? instruction : ("," + instruction));
				}
				ps.setString(6, instructions);
				ps.setDouble(7, recipe.getRating());
				ps.setString(8, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Remove Favorite Recipe Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove Favorite Recipe: " + recipe.getName() + " Successfully.");
			status = "true";
			comment = "Remove Favorite Recipe: " + recipe.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Favorite Recipe: " + recipe.getName() + " does not exist.");
		status = "false";
		comment = "Favorite Recipe: " + recipe.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}	
	
	private void closeOperators() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (st != null) {
				st.close();
			}
			if (ps != null) {
				ps.close();
			}
		} catch (SQLException sqle) {
			System.out.println("sqle: " + sqle.getMessage());
		}
	}

	private void disconnectMySQL() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException sqle) {
			System.out.println("Connection closing streams: " + sqle.getMessage());
		}
	}

}

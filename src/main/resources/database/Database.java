package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

import data.GroceryItem;
import data.GroceryList;
import data.ListItem;
import data.Recipe;
import data.Restaurant;
import data.UserInfo;
import data.UserList;

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
	public String[] signUpUser(UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedUser(user.getUsername())) {
			try {
				ps = conn.prepareStatement("insert into Userinfo(Username, UserPassword) VALUES(?, ?)");
				ps.setString(1, user.getUsername());
				// hash it
				HashPassword hash = new HashPassword();
				String hashedpasswrod = hash.getHashPassword(user.getPassword());
				ps.setString(2, hashedpasswrod);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				System.out.println("Sign-Up User Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("User: " + user.getUsername() + " SignUp Successfully.");
			status = "true";
			comment = "User: " + user.getUsername() + " SignUp Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Username has been used.");
		status =  "false";
		comment = "Username: " + user.getUsername() + " has been used.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	public String[] signInUser(UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];
		
		try {
			String sqlu = "SELECT * FROM Userinfo WHERE Username = ?";
			ps = conn.prepareStatement(sqlu);
			ps.setString(1, user.getUsername());
			rs = ps.executeQuery();
			//st = conn.createStatement();
			//rs = st.executeQuery("SELECT * FROM Userinfo WHERE Username='" + username + "';");
			// hash password
			HashPassword hash = new HashPassword();
			String hashedpasswrod = hash.getHashPassword(user.getPassword());
			while (rs.next()) {
				String sql = "SELECT * FROM Userinfo WHERE Username = ? AND UserPassword = ?";
				ps = conn.prepareStatement(sql);
				ps.setString(1, user.getUsername());
				ps.setString(2, hashedpasswrod);
				//ResultSet rs2 = st.executeQuery("SELECT * FROM Userinfo WHERE Username='" + username
				//		+ "' AND UserPassword='" + hashedpasswrod + "';");
				ResultSet rs2 = ps.executeQuery();
				while (rs2.next()) {
					System.out.println("User: " + user.getUsername() + " Login Successfully.");
					status = "true";
					comment = "User: " + user.getUsername() + " Login Successfully.";
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
		comment = "Username: " + user.getUsername() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	/* Handle Grocery Information */
	private boolean findExistedGroceryItem(GroceryItem groceryitem) { // check if groceryname exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM GroceryList WHERE GroceryName='" + groceryitem.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("GroceryName");
				if (gname.equals(groceryitem.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed Grocery Item Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	public String[] addGroceryItem(GroceryItem groceryitem, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedGroceryItem(groceryitem)) {
			try {
				ps = conn.prepareStatement(
						"insert into GroceryList(GroceryName, Amount, Units, Qualifier, Cost, Username) VALUES(?, ?, ?, ?, ?, ?)");

				ps.setString(1, groceryitem.getName());
				ps.setDouble(2, groceryitem.getAmount());
				ps.setString(3, groceryitem.getUnits());
				ps.setString(4, groceryitem.getQualifier());
				ps.setDouble(5, groceryitem.getCost());
				ps.setString(6, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Add Grocery Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Grocery: " + groceryitem.getName() + " is added.");
			status =  "true";
			comment = "Grocery: " + groceryitem.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Grocery is existed.");
		status =  "false";
		comment = "Grocery: " + groceryitem.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	public String[] removeGroceryItem(GroceryItem groceryitem, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedGroceryItem(groceryitem)) {
			try {
				String cmd = "DELETE FROM GroceryList WHERE GroceryName = ? AND Amount = ? AND Units = ? AND Qualifier = ? AND Cost = ? AND Username = ?";
				ps = conn.prepareStatement(cmd);
				ps.setString(1, groceryitem.getName());
				ps.setDouble(2, groceryitem.getAmount());
				ps.setString(3, groceryitem.getUnits());
				ps.setString(4, groceryitem.getQualifier());
				ps.setDouble(5, groceryitem.getCost());
				ps.setString(6, user.getUsername());
				ps.executeUpdate();
				
			} catch (SQLException sqle) {
				System.out.println("Remove GroceryItem Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove GroceryItem: " + groceryitem.getName() + " Successfully.");
			status = "true";
			comment = "Remove GroceryItem: " + groceryitem.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("GroceryItem: " + groceryitem.getName() + " does not exist.");
		status = "false";
		comment = "GroceryItem: " + groceryitem.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	public GroceryList getGroveryList(UserInfo user) {
		this.connection(); //connecting to database
		GroceryList grocerylist = new GroceryList();
		
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM GroceryList WHERE Username = '" + user.getUsername() + "';");
			while (rs.next()) {
				String name = rs.getString("GroceryName");
				double amount = rs.getDouble("Amount");
				String units = rs.getString("Units");
				String qualifier = rs.getString("Qualifier");
				double cost = rs.getDouble("Cost");
				
				GroceryItem groceryitem = new GroceryItem(name, amount, units, qualifier, cost);
				grocerylist.add(groceryitem);
			}
		} catch (SQLException sqle) {
			System.out.println("Get Grovery List Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		return grocerylist;
	}
	
	public void updateGroveryListOrder(GroceryList newgroverylist, UserInfo user) {
		this.connection(); //connecting to database
		
		try {
			String cmd = "DELETE FROM GroceryList WHERE Username = '" + user.getUsername() + "';";
			ps = conn.prepareStatement(cmd);
			ps.executeUpdate();
			
		} catch (SQLException sqle) {
			System.out.println("Remove GroceryItem Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		for(GroceryItem groceryitem: newgroverylist.getGroceries()) {
			addGroceryItem(groceryitem, user);
		}
		
	}
	
	/* Handle Favorite ListItem */
	public String[] addFavoriteListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return addFavoriteRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return addFavoriteRecipe(listitem.getRecipe(), user);
		}
	}
	
	public String[] removeFavoriteListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return removeFavoriteRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return removeFavoriteRecipe(listitem.getRecipe(), user);
		}
	}
	
	public UserList getFavoriteUserList(UserInfo user) {
		this.connection(); //connecting to database
		UserList userlist = new UserList();
		
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
				
				Restaurant restaurant = new Restaurant(name, url, price, address, phonenumner, rating, drivingTime);
				userlist.add(restaurant);
			}
		} catch (SQLException sqle) {
			System.out.println("Get Favorite Restaurant Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
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
				
				Recipe recipe = new Recipe(name, url, prepareTime, cookingTime, ingredients, instructions, rating);
				userlist.add(recipe);
			}
		} catch (SQLException sqle) {
			System.out.println("Get Favorite Recipe Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		return userlist;
	}
	
	// Update Favorite List Order
	public void updateFavoriteListOrder(ArrayList<ListItem> listitems, UserInfo user) {
		this.connection(); //connecting to database
		
		try {
			String cmd = "DELETE FROM FavoriteRestaurant WHERE Username = '" + user.getUsername() + "';";
			ps = conn.prepareStatement(cmd);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("Remove FavoriteRestaurant Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
		}
		
		try {
			String cmd = "DELETE FROM FavoriteRecipe WHERE Username = '" + user.getUsername() + "';";
			ps = conn.prepareStatement(cmd);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("Remove FavoriteRecipe Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		// update the new data
		for(int i = 0; i < listitems.size(); i++) {
			addFavoriteListItem(listitems.get(i), user);
		}
	}
	
	/* Handle Do not show ListItem */
	public String[] addDoNotShowListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return addDoNotShowRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return addDoNotShowRecipe(listitem.getRecipe(), user);
		}
	}
	
	public String[] removeDoNotShowListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return removeDoNotShowRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return removeDoNotShowRecipe(listitem.getRecipe(), user);
		}
	}
	
	public UserList getDoNotShowUserList(UserInfo user) {
		this.connection(); //connecting to database
		UserList userlist = new UserList();
		
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
				
				Restaurant restaurant = new Restaurant(name, url, price, address, phonenumner, rating, drivingTime);
				userlist.add(restaurant);
			}
		} catch (SQLException sqle) {
			System.out.println("Get Do Not Show Restaurant Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
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
				
				Recipe recipe = new Recipe(name, url, prepareTime, cookingTime, ingredients, instructions, rating);
				userlist.add(recipe);
			}
		} catch (SQLException sqle) {
			System.out.println("Get Do Not Show Recipe Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		return userlist;
	}
	
	// Update Do Not Show List Order
	public void updateDoNotShowListOrder(ArrayList<ListItem> listitems, UserInfo user) {
		this.connection(); //connecting to database
		
		try {
			String cmd = "DELETE FROM DoNotShowRestaurant WHERE Username = '" + user.getUsername() + "';";
			ps = conn.prepareStatement(cmd);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("Remove DoNotShowRestaurant Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
		}
		
		try {
			String cmd = "DELETE FROM DoNotShowRecipe WHERE Username = '" + user.getUsername() + "';";
			ps = conn.prepareStatement(cmd);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println("Remove DoNotShowRecipe Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		// update the new data
		for(int i = 0; i < listitems.size(); i++) {
			addDoNotShowListItem(listitems.get(i), user);
		}
	}
	
	/* Handle To Explore ListItem */
	public String[] addToExploreListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return addToExploreRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return addToExploreRecipe(listitem.getRecipe(), user);
		}
	}
	
	public String[] removeToExploreListItem(ListItem listitem, UserInfo user) {
		if(listitem.getType() == "res") {
			return removeToExploreRestaurant(listitem.getRestaurant(), user);
		} else { // if listitem.getType() == "rep" 
			return removeToExploreRecipe(listitem.getRecipe(), user);
		}
	}
	
	public UserList getToExploreUserList(UserInfo user) {
		this.connection(); //connecting to database
		UserList userlist = new UserList();
		
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
				
				Restaurant restaurant = new Restaurant(name, url, price, address, phonenumner, rating, drivingTime);
				userlist.add(restaurant);
			}
		} catch (SQLException sqle) {
			System.out.println("Get To Explore Restaurant Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
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
				
				Recipe recipe = new Recipe(name, url, prepareTime, cookingTime, ingredients, instructions, rating);
				userlist.add(recipe);
			}
		} catch (SQLException sqle) {
			System.out.println("Get To Explore Recipe Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		
		return userlist;
	}
	
	// Update To Explore List Order
		public void updateToExploreListOrder(ArrayList<ListItem> listitems, UserInfo user) {
			this.connection(); //connecting to database
			
			try {
				String cmd = "DELETE FROM ToExploreRestaurant WHERE Username = '" + user.getUsername() + "';";
				ps = conn.prepareStatement(cmd);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				System.out.println("Remove ToExploreRestaurant Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
			}
			
			try {
				String cmd = "DELETE FROM ToExploreRecipe WHERE Username = '" + user.getUsername() + "';";
				ps = conn.prepareStatement(cmd);
				ps.executeUpdate();
			} catch (SQLException sqle) {
				System.out.println("Remove ToExploreRecipe Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			
			// update the new data
			for(int i = 0; i < listitems.size(); i++) {
				addToExploreListItem(listitems.get(i), user);
			}
		}
	
	
	/* ***************************************************************
	 * Functions below this line will not be used for Front-End      *
	 * 																 *
	 * ***************************************************************/
	
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
	
	private String[] addFavoriteRestaurant(Restaurant restaurant, UserInfo user) {
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

	private String[] removeFavoriteRestaurant(Restaurant restaurant, UserInfo user) {
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
	
	private String[] addFavoriteRecipe(Recipe recipe, UserInfo user) {
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

	private String[] removeFavoriteRecipe(Recipe recipe, UserInfo user) {
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
	
	/* Handle Do Not Show Restaurant */
	private boolean findExistedDoNotShowRestaurant(Restaurant restaurant) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM DoNotShowRestaurant WHERE DNSRName='" + restaurant.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("DNSRName");
				if (gname.equals(restaurant.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find So Not Show Restaurant Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	private String[] addDoNotShowRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedDoNotShowRestaurant(restaurant)) {
			try {
				ps = conn.prepareStatement(
						"insert into DoNotShowRestaurant(DNSRName, DNSRUrl, DNSRPrice, DNSRAddress, DNSRPhoneNumber, DNSRRating, DNSRDrivingTime, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

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
				System.out.println("Add Do Not Show Restaurant Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Do Not Show Restaurant: " + restaurant.getName() + " is added.");
			status =  "true";
			comment = "Do Not Show Restaurant: " + restaurant.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Do Not Show Restaurant is existed.");
		status =  "false";
		comment = "Do Not Show Restauran: " + restaurant.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	private String[] removeDoNotShowRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedDoNotShowRestaurant(restaurant)) {
			try {
				String cmd = "DELETE FROM DoNotShowRestaurant WHERE DNSRName = ? AND DNSRUrl = ? AND DNSRPrice = ? AND DNSRAddress = ? AND DNSRPhoneNumber = ? AND DNSRRating = ? AND DNSRDrivingTime = ? AND Username = ?";
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
				System.out.println("Remove Do Not Show Restaurant Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove Do Not Show Restaurant: " + restaurant.getName() + " Successfully.");
			status = "true";
			comment = "Remove Do Not Show Restaurant: " + restaurant.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Do Not Show Restaurant: " + restaurant.getName() + " does not exist.");
		status = "false";
		comment = "Do Not Show Restaurant: " + restaurant.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	/* Handle Do Not Show Recipe */
	private boolean findExistedSoNotShowRecipe(Recipe recipe) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM DoNotShowRecipe WHERE DNSPName='" + recipe.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("DNSPName");
				if (gname.equals(recipe.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed Do Not Show Recipe Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	private String[] addDoNotShowRecipe(Recipe recipe, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedSoNotShowRecipe(recipe)) {
			try {
				ps = conn.prepareStatement(
						"insert into DoNotShowRecipe(DNSPName, DNSPUrl, DNSPPrepareTime, DNSPCookingTime, DNSPIngredients, DNSPInstructions, DNSPRating, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

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
				System.out.println("Add Do Not Show Recipe Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Do Not Show Recipe: " + recipe.getName() + " is added.");
			status =  "true";
			comment = "Do Not Show Recipe: " + recipe.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Do Not Show Recipe is existed.");
		status =  "false";
		comment = "Do Not Show Recipe: " + recipe.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	private String[] removeDoNotShowRecipe(Recipe recipe, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedSoNotShowRecipe(recipe)) {
			try {
				String cmd = "DELETE FROM DoNotShowRecipe WHERE DNSPName = ? AND DNSPUrl = ? AND DNSPPrepareTime = ? AND DNSPCookingTime = ? AND DNSPIngredients = ? AND DNSPInstructions = ? AND DNSPRating = ? AND Username = ?";
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
				System.out.println("Remove Do Not Show Recipe Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove Do Not Show Recipe: " + recipe.getName() + " Successfully.");
			status = "true";
			comment = "Remove Do Not Show Recipe: " + recipe.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Do Not Show Recipe: " + recipe.getName() + " does not exist.");
		status = "false";
		comment = "Do Not Show Recipe: " + recipe.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}	
	
	/* Handle Favorite Restaurant */
	private boolean findExistedToExploreRestaurant(Restaurant restaurant) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM ToExploreRestaurant WHERE TERName='" + restaurant.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("TERName");
				if (gname.equals(restaurant.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed To Explore Restaurant Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	private String[] addToExploreRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedToExploreRestaurant(restaurant)) {
			try {
				ps = conn.prepareStatement(
						"insert into ToExploreRestaurant(TERName, TERUrl, TERPrice, TERAddress, TERPhoneNumber, TERRating, TERDrivingTime, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

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
				System.out.println("Add To Explore Restaurant Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("To Explore Restaurant: " + restaurant.getName() + " is added.");
			status =  "true";
			comment = "To Explore Restaurant: " + restaurant.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: To Explore Restaurant is existed.");
		status =  "false";
		comment = "To Explore Restauran: " + restaurant.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	private String[] removeToExploreRestaurant(Restaurant restaurant, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedToExploreRestaurant(restaurant)) {
			try {
				String cmd = "DELETE FROM ToExploreRestaurant WHERE TERName = ? AND TERUrl = ? AND TERPrice = ? AND TERAddress = ? AND TERPhoneNumber = ? AND TERRating = ? AND TERDrivingTime = ? AND Username = ?";
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
				System.out.println("Remove To Explore Restaurant Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove To Explore Restaurant: " + restaurant.getName() + " Successfully.");
			status = "true";
			comment = "Remove To Explore Restaurant: " + restaurant.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("To Explore Restaurant: " + restaurant.getName() + " does not exist.");
		status = "false";
		comment = "To Explore Restaurant: " + restaurant.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
	/* Handle To Explore Recipe */
	private boolean findExistedToExploreRecipe(Recipe recipe) { // check if restaurant name exists
		boolean existed = false;
		try {
			st = conn.createStatement();
			rs = st.executeQuery("SELECT * FROM ToExploreRecipe WHERE TEPName='" + recipe.getName() + "';");
			while (rs.next()) {
				String gname = rs.getString("TEPName");
				if (gname.equals(recipe.getName()))
					existed = true;
			}
		} catch (SQLException sqle) {
			System.out.println("Find Existed To Explore Recipe Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	private String[] addToExploreRecipe(Recipe recipe, UserInfo user) {
		this.connection(); //connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (!this.findExistedToExploreRecipe(recipe)) {
			try {
				ps = conn.prepareStatement(
						"insert into ToExploreRecipe(TEPName, TEPUrl, TEPPrepareTime, TEPCookingTime, TEPIngredients, TEPInstructions, TEPRating, Username) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

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
				System.out.println("Add To Explore Recipe Exception :" + sqle.toString());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("To Explore Recipe: " + recipe.getName() + " is added.");
			status =  "true";
			comment = "To Explore Recipe: " + recipe.getName() + " is added.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: To Explore Recipe is existed.");
		status =  "false";
		comment = "To Explore Recipe: " + recipe.getName() + " is existed.";
		response[0] = status;
		response[1] = comment;
		return response;
	}

	private String[] removeToExploreRecipe(Recipe recipe, UserInfo user) {
		this.connection(); // connecting to database
		String status = "";
		String comment = "";
		String[] response = new String[2];

		if (findExistedToExploreRecipe(recipe)) {
			try {
				String cmd = "DELETE FROM ToExploreRecipe WHERE TEPName = ? AND TEPUrl = ? AND TEPPrepareTime = ? AND TEPCookingTime = ? AND TEPIngredients = ? AND TEPInstructions = ? AND TEPRating = ? AND Username = ?";
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
				System.out.println("Remove To Explore Recipe Exception: " + sqle.getMessage());
			} finally {
				this.closeOperators();
				this.disconnectMySQL();
			}
			System.out.println("Remove To Explore Recipe: " + recipe.getName() + " Successfully.");
			status = "true";
			comment = "Remove To Explore Recipe: " + recipe.getName() + " Successfully.";
			response[0] = status;
			response[1] = comment;
			return response;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("To Explore Recipe: " + recipe.getName() + " does not exist.");
		status = "false";
		comment = "To Explore Recipe: " + recipe.getName() + " does not exist.";
		response[0] = status;
		response[1] = comment;
		return response;
	}
	
//	private JSONObject getJsonObject(boolean status, String comment) {
//	JSONObject obj = new JSONObject();
//	obj.put("Status", status); // return boolean
//	obj.put("Comment", comment); // return comment
//	
//	return obj;
//}
	
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

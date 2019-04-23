package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;

public class Database {
	
	private Connection conn = null;
	private Statement st = null;
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	public Database(){
	  connection();
	}
	private void connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Info?user=root&password=root");
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
		String status = "false";
		String comment = "";

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
			status =  "true";
			comment = "User: " + username + " SignUp Successfully.";
			String[] stringArray = new String[2];
	    stringArray[0] = status;
	    stringArray[1] = comment;
			return stringArray;
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Username has been used.");
		status =  "false";
		comment = "Username: " + username + " has been used.";
		String[] stringArray = new String[2];
		stringArray[0] = status;
		stringArray[1] = comment;
		return stringArray;
	}
	
	public JSONObject signInUser(String username, String password) {
		this.connection(); //connecting to database
		boolean status = false;
		String comment = "";
		
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
					status = true;
					comment = "User: " + username + " Login Successfully.";
					return getJsonObject(status, comment);
				}
				System.out.println("Password is Incorrect.");
				status = false;
				comment = "Password is Incorrect.";
				return getJsonObject(status, comment);
			}
		} catch (SQLException sqle) {
			System.out.println("Sign-In User Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		System.out.println("Username does not exist.");
		status =  false;
		comment = "Username: " + username + " does not exist.";
		
		return getJsonObject(status, comment);
	}
	
	private JSONObject getJsonObject(boolean status, String comment) {
		JSONObject obj = new JSONObject();
		obj.put("Status", status); // return boolean
		obj.put("Comment", comment); // return comment
		
		return obj;
	}
	
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
			System.out.println("Sign-In User Exception: " + sqle.getMessage());
		} 
		return existed;
	}
	
	public JSONObject addGroceryItem(String groceryname, double amount, String units, String qualifier, double cost, String username) {
		this.connection(); //connecting to database
		boolean status = false;
		String comment = "";

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
			status =  true;
			comment = "Grocery: " + groceryname + " is added.";
			return getJsonObject(status, comment);
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("Error: Grocery is existed.");
		status =  false;
		comment = "Grocery: " + groceryname + " is existed.";
		
		return getJsonObject(status, comment);
	}

	public JSONObject removeGroceryItem(String groceryname, double amount, String units, String qualifier, double cost, String username) {
		this.connection(); //connecting to database
		boolean status = false;
		String comment = "";

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
			status = true;
			comment = "Remove GroceryItem: " + groceryname + " Successfully.";
			return getJsonObject(status, comment);
		}
		this.closeOperators();
		this.disconnectMySQL();
		
		System.out.println("GroceryItem: " + groceryname + " does not exist.");
		status = false;
		comment = "GroceryItem: " + groceryname + " does not exist.";
		return getJsonObject(status, comment);
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

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
	public JSONObject signUpUser(String username, String password) {
		this.connection(); //connecting to database
		boolean status = false;
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
			status =  true;
			comment = "User: " + username + " SignUp Successfully.";
			return getJsonObject(status, comment);
		}
		System.out.println("Error: Username has been used.");
		status =  false;
		comment = "Username: " + username + " has been used.";
		
		return getJsonObject(status, comment);
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

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
	public boolean signUpUser(String username, String password) {
		this.connection(); //connecting to database

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
			System.out.println("User: " + username + " SignUp Successfully");
			return true;
		}
		System.out.println("Error: Username has been used.");
		return false;
	}
	
	public boolean signInUser(String username, String password) {
		this.connection(); //connecting to database
		
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
					return true;
				}
				System.out.println("Password Incorrect.");
				return false;
			}
		} catch (SQLException sqle) {
			System.out.println("Sign-In User Exception: " + sqle.getMessage());
		} finally {
			this.closeOperators();
			this.disconnectMySQL();
		}
		System.out.println("Username does not exist.");
		return false;
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

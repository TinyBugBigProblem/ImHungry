package databasetests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import data.UserInfo;
import database.Database;

public class SecureTest {
	
	private Connection conn;
	private PreparedStatement ps;
	
	private Database database;
	private UserInfo user;
	private String username;
	private String password;
	
	@Before
	public void setup() throws Exception {
		
		conn = null;
		ps = null;
		
		database = new Database();
		username =  "VincentSecure";
		password = "Secure123";
		user = new UserInfo(username, password);
		database.signUpUser(user);
	}

	@Test
	public void testSignInWithoutHash() throws SQLException {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Info?user=root&password=root&useSSL=false");
		} catch (SQLException sqle) {
			System.out.println("SQLException: " + sqle.getMessage());
		} catch (ClassNotFoundException cnfe) {
			System.out.println("ClassNotFoundException: " + cnfe.getMessage());
		}
		
		boolean checkSignIn = false;

		try {
			String sql = "SELECT * FROM Userinfo WHERE Username = ? AND UserPassword = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ResultSet rs2 = ps.executeQuery();
			while (rs2.next()) {
				checkSignIn = true;
				return;
			}
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException sqle) {
				System.out.println("sqle: " + sqle.getMessage());
			}
		}
		assertFalse(checkSignIn);
		
	}
	
	@Test
	public void testInjectionCode() {
		
		String[] str = new String[2];		
		// code injection attack
		String username = "1' OR '1' = '1";
		String password = "1' OR '1' = '1";
		UserInfo user1 = new UserInfo(username, password);
		str = database.signInUser(user1);
		assertEquals("false", str[0]);
		
		//drop database
		username = "1'; DROP DATABASE Info;";
		UserInfo user2 = new UserInfo(username, password);
		str = database.signInUser(user2);
		assertEquals("false", str[0]);

	}
}

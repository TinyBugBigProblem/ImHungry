package databasetests;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import database.HashPassword;

/*
 * Tests for UserInfo class.
 */
public class HashPasswordTest {
	
	private String password;

	
	@Before
	public void setup() {
		password = "csci310";
	}
	
	@Test
	public void testConstructor() {
		
		HashPassword hash = new HashPassword();
		String hashpassword = hash.getHashPassword(password);
		assertFalse(password == hashpassword);
		
	}

	@Test
	public void testEquals() {
		
		String password2 = "Csci310";
		String password3 = "csci3100";
		HashPassword hash = new HashPassword();
		String hashpassword = hash.getHashPassword(password);
		String hashpassword2 = hash.getHashPassword(password2);
		String hashpassword3 = hash.getHashPassword(password3);
		
		assertFalse(hashpassword == hashpassword2);
		assertFalse(hashpassword == hashpassword3);
		assertFalse(hashpassword2 == hashpassword3);
		
		assertFalse(password2 == hashpassword2);
		assertFalse(password3 == hashpassword3);

	}
	
}

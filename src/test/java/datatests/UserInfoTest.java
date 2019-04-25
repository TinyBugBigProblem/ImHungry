package datatests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import data.UserInfo;

/*
 * Tests for UserInfo class.
 */
public class UserInfoTest {
	
	private String username;
	private String password;
	private UserInfo info;
	
	@Before
	public void setup() {
		username = "Vincent";
		password = "USC";
		
		info = new UserInfo(username, password);
	}
	

	@Test
	public void testConstructor() {
		UserInfo userinfo = new UserInfo(username, password);
		
		assertEquals(username, userinfo.getUsername());
		assertEquals(password, userinfo.getPassword());
	}

	@Test
	public void testEquals() {
		
		UserInfo userinfo1 = new UserInfo("Vincent", "USC");
		UserInfo userinfo2 = new UserInfo("Vincent2", "USC2");
		
		ArrayList<UserInfo> userlist = new ArrayList<UserInfo>();
		
		assertTrue(userlist.isEmpty());
		
		userlist.add(userinfo1);

		assertFalse(userlist.isEmpty());
		assertTrue(userlist.contains(userinfo1));
		assertFalse(userlist.contains(userinfo2));

	}
	
	@Test
	public void testGetFunction() throws IOException {
		assertEquals(username, info.getUsername());
		assertEquals(password, info.getPassword());
		
		assertFalse(username == info.getPassword());
		assertFalse(password == info.getUsername());
	}
	
}

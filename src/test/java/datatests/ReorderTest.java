package datatests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import data.Restaurant;
import data.UserList;


/*
 * Tests for the AccessYelpAPI class.
 */
public class ReorderTest {
	
	private UserList ulist;
	Restaurant usc;
	Restaurant ucla;
	Restaurant stanford;
	Restaurant berkeley;

	@Before
	public void setup() throws Exception {
		ulist = new UserList();
		usc = new Restaurant("USC", "usc.edu", 5, "USC Campus", "123456789", 5.0, 3);
		ucla = new Restaurant("UCLA", "ucla.edu", 2, "UCLA Campus", "987654321", 0.1, 10);
		stanford = new Restaurant("Stanford", "stanford.edu", 5, "Stanford Campus", "456789123", 5.0, 20);
		berkeley = new Restaurant("Berkeley", "berkeley.edu", 2, "Berkeley Campus", "789123456", 5.0, 30);
		
		ulist.add(usc);
		ulist.add(ucla);
		ulist.add(stanford);
		ulist.add(berkeley);
	}

	@Test
	public void testReorderNearby() throws IOException {
		assertEquals(0, ulist.getArrayNum(usc));
		assertEquals(1, ulist.getArrayNum(ucla));
		ulist.getRestaurants().set(1, usc);
		ulist.getRestaurants().set(0, ucla);
		assertEquals(1, ulist.getArrayNum(usc));
		assertEquals(0, ulist.getArrayNum(ucla));
	}
	
	@Test
	public void testSwitch() throws IOException {
		assertEquals(0, ulist.getArrayNum(usc));
		assertEquals(2, ulist.getArrayNum(stanford));
		ulist.getRestaurants().set(2, usc);
		ulist.getRestaurants().set(0, stanford);
		assertEquals(2, ulist.getArrayNum(usc));
		assertEquals(0, ulist.getArrayNum(stanford));
	}
	
	@Test
	public void testAddDuplicate() throws IOException {
		assertEquals(4, ulist.getRestaurants().size());
		ulist.add(usc);
		assertEquals(4, ulist.getRestaurants().size());
	}
	
	@Test
	public void testRemove() throws IOException {
		assertEquals(4, ulist.getRestaurants().size());
		ulist.remove(usc);
		assertEquals(3, ulist.getRestaurants().size());
	}
	
	@Test
	public void testReturnNumber() throws IOException {
		assertEquals(0, ulist.getArrayNum(usc));
		ulist.remove(usc);
		assertEquals(-1, ulist.getArrayNum(usc));
	}
	
}
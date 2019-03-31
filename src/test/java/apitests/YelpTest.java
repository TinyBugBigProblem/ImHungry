package apitests;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.junit.Test;

import api.AccessYelpAPI;
import api.GoogleDirections;
import data.Restaurant;

public class YelpTest {

	@Test
	public void testBasicFunctionality() throws IOException {
		
		// This is necessary for complete coverage, even though the functions are supposed to be accessed statically.
		AccessYelpAPI y = new AccessYelpAPI();
		
		int count = 20;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("mexican", count);
		assertEquals(count, arr.size());
		
		for (int i = 0; i < count; i++) {
			System.out.println(i);
			System.out.println("name: " + arr.get(i).getName());
			System.out.println("driveTime: " + arr.get(i).getDrivingTime());
		}
	}

	@Test
	public void testMultipleWordInput() throws IOException {
		int count = 20;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("mexican chicken", count);
		assertEquals(count, arr.size());
		
		for (int i = 0; i < count; i++) {
			System.out.println(i);
			System.out.println("name: " + arr.get(i).getName());
			System.out.println("driveTime: " + arr.get(i).getDrivingTime());
		}
	}
	
	@Test
	public void testBadInput() throws IOException {
		int count = 20;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("qwertyuioiuytrewwdc", count);
		assertEquals(0, arr.size());
		
	}
	
	@Test
	public void testLargeInput() throws IOException {
		int count = 55;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("pizza", count);
		assertEquals(55, arr.size());
		
	}

}

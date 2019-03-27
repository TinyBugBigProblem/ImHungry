package apitests;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Vector;

import org.junit.Test;

import api.AccessYelpAPI;
import data.Restaurant;

/*
 * Tests for the AccessYelpAPI class.
 */
public class RadiusTest {

	/*
	 *  Test to make sure that no errors will be thrown if there are no results from the input.
	 */
	@Test
	public void testZeroRadiusInput() throws IOException {
		int count = 50;
		int radius = 0;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(0, arr.size());
		
	}
	
	/*
	 *  Test to make sure that correct number of Restaurant will be returned 
	 *  if pass in the requested radius.
	 */
	@Test
	public void testNormalRadiusInput() throws IOException {
		int count = 50;
		int radius = 1000;
		Vector<Restaurant> arr = AccessYelpAPI.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(10, arr.size()); // I guess 10??
		
	}	

}
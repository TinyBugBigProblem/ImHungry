package apitests;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import api.AccessYelpAPI;
import data.Restaurant;

/*
 * Tests for the radius input in AccessYelpAPI class.
 */
public class RadiusTest {
	
	private AccessYelpAPI yelpSearch;
	private int count;

	@Before
	public void setup() throws Exception {
		yelpSearch = new AccessYelpAPI();
		count = 30;
	}

	/*
	 *  Test to make sure that no errors will be thrown if there are no results from the input.
	 */
	@Test
	public void testZeroRadiusInput() throws IOException {
		double radius = 0.0; // search by miles: MAX number is 25 miles 
		Vector<Restaurant> restaurantList = yelpSearch.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(0, restaurantList.size());
	}
	
	/*
	 *  Test to make sure that correct number of Restaurant will be returned 
	 *  if pass in double radius.
	 */
	@Test
	public void testDoubleInput() throws IOException {
		double radius = 0.1; // search by miles: MAX number is 25 miles 
		Vector<Restaurant> restaurantList = yelpSearch.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(2, restaurantList.size());
	}
	
	/*
	 *  Test to make sure that correct number of Restaurant will be returned 
	 *  if pass in the requested radius.
	 */
	@Test
	public void testNormalRadiusInput() throws IOException {
		double radius = 25.0; // search by miles: MAX number is 25 miles 
		Vector<Restaurant> restaurantList = yelpSearch.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(30, restaurantList.size());
		
	}
	
	/*
	 *  Test to make sure that no errors will be thrown if input the radius more than MAX number.
	 */
	@Test
	public void testOverMaxInput() throws IOException {
		double radius = 50.0; // search by miles: MAX number is 25 miles 
		Vector<Restaurant> restaurantList = yelpSearch.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(30, restaurantList.size());
		
	}	

	/*
	 *  Test to make sure that no errors will be thrown if input negative radius.
	 */
	@Test
	public void testNegativeInput() throws IOException {
		double radius = -5.0; // search by miles: MAX number is 25 miles 
		Vector<Restaurant> restaurantList = yelpSearch.YelpRestaurantSearch("pizza", count, radius);
		assertEquals(0, restaurantList.size());
		
	}	
	
}

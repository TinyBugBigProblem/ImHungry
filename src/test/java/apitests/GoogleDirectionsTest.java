package apitests;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import api.GoogleDirections;

public class GoogleDirectionsTest {

	@Test
	public void testBasicFunctionality() throws IOException {
		
		// This is necessary for complete coverage, even though the functions are supposed to be accessed statically.
		GoogleDirections gd = new GoogleDirections();
		
		int timeDuration = GoogleDirections.getDrivingTime(34.0206, -118.2854, 34.0252, -118.2788);
		System.out.println(timeDuration);
		assertTrue(timeDuration > 120);
	}
	
	@Test
	public void badUrltest() throws IOException {
		
		int timeDuration = GoogleDirections.getDrivingTime(34000.0206, -118.2854, 34.0252, -118.2788);
		System.out.println(timeDuration);
		assertEquals(-1, timeDuration);
	}

}

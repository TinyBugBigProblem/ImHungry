package apitests;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.junit.Test;

import api.GoogleDirections;
import api.GoogleImageSearch;

public class GoogleImageSearchTest {

	@Test
	public void testBasicFunctionality() throws IOException {
		// This is necessary for complete coverage, even though the functions are supposed to be accessed statically.
		GoogleImageSearch gi = new GoogleImageSearch();
		
		Vector<String> arr = GoogleImageSearch.GetImagesFromGoogle("mexican");
		assertEquals(10, arr.size());
	}

	@Test
	public void testMultipleWordInput() throws IOException {
		Vector<String> arr = GoogleImageSearch.GetImagesFromGoogle("mexican food");
		assertEquals(10, arr.size());

	}

	@Test
	public void testBadInput() throws IOException {
		Vector<String> arr = GoogleImageSearch.GetImagesFromGoogle("qwertyuiopoiuyrtyuiopoiuyghjuytfvb");
		assertEquals(0, arr.size());

	}

}

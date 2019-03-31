package apitests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.junit.Test;

import api.GoogleDirections;
import api.Scrapper;
import data.Recipe;

public class ScrapperTest {

	@Test
	public void parseTimeTest() {
		int value = Scrapper.parseTime("PT1H");
		assertEquals(value, 60);
		
		value = Scrapper.parseTime("PT1H10M");
		assertEquals(value, 70);
		
		value = Scrapper.parseTime("PT1H5M");
		assertEquals(value, 65);
		
		value = Scrapper.parseTime("PT15M");
		assertEquals(value, 15);
		
		value = Scrapper.parseTime("PT5M");
		assertEquals(value, 5);
		
		value = Scrapper.parseTime("");
		assertEquals(value, -1);
		
		value = Scrapper.parseTime("PT");
		assertEquals(value, -1);
		
	}
	
	
	@Test
	public void getTest() throws IOException {
		
		// This is necessary for complete coverage, even though the functions are supposed to be accessed statically.
		Scrapper sr = new Scrapper();
		
		String url = "https://www.allrecipes.com/recipe/228293/curry-stand-chicken-tikka-masala-sauce/";
		
		Recipe recipe = Scrapper.get(url);
		
		System.out.println("Results for recipe at " + url);
		System.out.println("Name: " + recipe.getName());
		System.out.println("Picture url: " + recipe.getPictureUrl());
		System.out.println("Prep Time: " + recipe.getPrepTime());
		System.out.println("Cook Time: " + recipe.getCookTime());
		System.out.println("Rating: " + recipe.getRating());
		System.out.println("Ingredients: " + recipe.getIngredients().size());
		for(String ingredient : recipe.getIngredients()) {
			System.out.println("    " + ingredient);
		}
		System.out.println("Instructions: " + recipe.getInstructions().size());
		for(String instruction : recipe.getInstructions()) {
			System.out.println("    " + instruction);
		}
		
	}
	
	
	@Test
	public void searchTest() throws IOException {
		Vector<Recipe> recipes = Scrapper.search("chicken", 5);
		assertEquals(5, recipes.size());
		
		recipes = Scrapper.search("broccoli", 21);
		assertEquals(21, recipes.size());
		
	}
	
	@Test
	public void searchBadRequestTest() throws IOException {
		Vector<Recipe> recipes = Scrapper.search("qwertyuiop", 25);
		assertEquals(0, recipes.size());
		
		recipes = Scrapper.search("shoe", 25);
		assertEquals(3, recipes.size());
		
	}
	
	@Test
	public void searchMultipleTermsTest() throws IOException {
		Vector<Recipe> recipes = Scrapper.search("chicken curry", 2);
		assertEquals(2, recipes.size());
		
	}
	
	
}

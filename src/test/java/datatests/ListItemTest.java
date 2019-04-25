package datatests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import data.ListItem;
import data.Restaurant;
import data.Recipe;

public class ListItemTest {
	
	@Test
	public void listItemTest() {
		String name = "Bob";
		String pictureUrl = "www.what.com";
		double prepTime = 12;
		double cookTime = 13;
		ArrayList<String> ingredients = null;
		ArrayList<String> instructions = null;
		double rating = 4;
		
		Recipe rec = new Recipe(name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);
		ListItem item = new ListItem(null, null, "none");
		
		item.setRecipe(rec);
		Recipe newRec = item.getRecipe();
		assertTrue(newRec.equals(rec));
		
		item.setRestaurant(null);
		Restaurant newRes = item.getRestaurant();
		assertEquals(newRes, null);
		
		String type = item.getType();
		// assertTrue(type == "res");
		assertEquals(type,"res");

		
		
	}

}

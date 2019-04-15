package datatests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.junit.Test;

import data.GroceryItem;
import data.GroceryList;
import data.Restaurant;

public class GroceryItemTest {

	@Test
	public void testConstructor() {
		String name = "Milk";
		double amount = 1;
		String units = "Gallon";
		String qualifier = "Fat Free";
		double cost = 5;
		
		GroceryItem food = new GroceryItem(name, amount, units, qualifier, cost);
		
		assertEquals(name, food.getName());
		assertEquals(amount, food.getAmount(), 0);	// 3rd argument of 0 is because of how java does doubles' precision
		assertEquals(units, food.getUnits());
		assertEquals(qualifier, food.getQualifier());
		assertEquals(cost, food.getCost(), 0);
	}
	
	@Test
	public void listTest() {
		GroceryList groceries = new GroceryList();
		GroceryItem food1 = new GroceryItem("milk", 1, "Gallon", "Fat Free", 5);
		GroceryItem food2 = new GroceryItem("eggs", 12, "Egg", "", 1);
		GroceryItem food3 = new GroceryItem("butter", 1, "Tsp", "Salted", 5);
		GroceryItem food4 = new GroceryItem("sugar", 1, "cup", "Fat Free", 5);
		GroceryItem food5 = new GroceryItem("salt", 1, "pinch", "", 5);
		groceries.add(food1);
		groceries.add(food2);
		groceries.add(food3);
		groceries.add(food4);
		assertTrue(groceries.add(food5));
		assertEquals(0, groceries.getArrayNum(food1));
		assertFalse(groceries.add(food1));
		assertEquals(4, groceries.getArrayNum(food5));
		assertFalse(groceries.add(food5));
		assertTrue(groceries.remove(food5));
		assertFalse(groceries.remove(food5));
		assertTrue(groceries.contains(food4));
		assertFalse(groceries.contains(food5));
		assertFalse(food1.equals(groceries));

	}
	
	@Test
	public void MoreGroceryListTest() {
		
		GroceryList groceries = new GroceryList();
		GroceryItem food1 = new GroceryItem("milk", 1, "Gallon", "Fat Free", 5);
		GroceryItem food2 = new GroceryItem("eggs", 12, "Egg", "", 1);
		GroceryItem food3 = new GroceryItem("butter", 1, "Tsp", "Salted", 5);
		GroceryItem food4 = new GroceryItem("sugar", 1, "cup", "Fat Free", 5);
		GroceryItem food5 = new GroceryItem("salt", 1, "pinch", "", 5);
		GroceryItem food6 = new GroceryItem("pepper", 1, "pinch", "", 5);
		groceries.add(food1);
		groceries.add(food2);
		groceries.add(food3);
		groceries.add(food4);
		groceries.add(food5);
		assertEquals(groceries.getGroceries().size(), 5);
		assertEquals(groceries.getArrayNum(food6),-1);
	}

}

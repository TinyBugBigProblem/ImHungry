package datatests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import org.junit.Test;

import data.GroceryItem;

public class GroceryItemTest {

	@Test
	public void testConstructor() {
		String name = "Milk";
		double amount = 1;
		String units = "Gallon";
		String qualifier = "Fat Free";
		double cost = 5;
		
		GroceryItem food = new GroceryItem(name, amount, units);
		
		assertEquals(name, food.getName());
		assertEquals(amount, food.getAmount(), 0);	// 3rd argument of 0 is because of how java does doubles' precision
		assertEquals(units, food.getUnits());
	}

}

package datatests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GroceryItemTest.class, RecipeTest.class, ReorderTest.class, RestaurantTest.class, UserListTest.class })
public class DataTests {

}

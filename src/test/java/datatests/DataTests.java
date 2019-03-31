package datatests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RecipeTest.class, RestaurantTest.class, UserListTest.class })
public class DataTests {

}

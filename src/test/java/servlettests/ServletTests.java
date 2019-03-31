package servlettests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ListManagementPageServletTest.class, RecipeDetailsPagePrintableVersionServletTest.class, RecipeDetailsPageServletTest.class,
	RestaurantDetailsPagePrintableVersionServletTest.class, RestaurantDetailsPageServletTest.class, ResultsPageServletTest.class, SearchPageServletTest.class })
public class ServletTests {

}

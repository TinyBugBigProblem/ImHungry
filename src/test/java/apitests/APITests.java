package apitests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ GoogleDirectionsTest.class, GoogleImageSearchTest.class, RadiusTest.class, ScrapperTest.class, YelpTest.class })
public class APITests {

}

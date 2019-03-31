package servlettests;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import data.Recipe;
import data.Restaurant;
import data.UserList;
import servlets.ResultsPageServlet;

public class ResultsPageServletTest {

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;
	private Recipe recipe1;
	private Recipe recipe2;
	private Restaurant restaurant1;
	private Restaurant restaurant2;
	private UserList[] userLists;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		rd = mock(RequestDispatcher.class);
		
		when(request.getSession()).thenReturn(session);
		when(request.getRequestDispatcher("/jsp/results.jsp")).thenReturn(rd);
		
		String name = "Good Food";
		String pictureUrl = "http://www.todayifoundout.com/wp-content/uploads/2017/11/rick-astley.png";
		double prepTime = 10;
		double cookTime = 25;
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("1 teaspoon ground ginger");
		ingredients.add("1 rack of lamb");
		ArrayList<String> instructions = new ArrayList<String>();
		instructions.add("Throw in a pan.");
		instructions.add("Cook until done.");
		double rating = 4.5;
		
		recipe1 = new Recipe(name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);
		recipe2 = new Recipe("Not" + name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);

		restaurant1 = new Restaurant("A Good Restaurant", "https://www.mcdonalds.com/us/en-us.html", 1, "Everywhere", "(123)456-7890", 2.25, 5);
		restaurant2 = new Restaurant("A Bad Restaurant", "https://www.bk.com/", 2, "Almost everywhere", "(123)456-7896", 1.25, 50);
		
		userLists = new UserList[3];
		for (int i = 0; i < 3; ++i) {
			userLists[i] = new UserList();
		}		
		
	}

	@Test
	public void testFromSearch() throws Exception {
		
		// Add to Favorites
		userLists[0].add(recipe1);
		userLists[0].add(restaurant1);
		
		// Add to Do Not Show
		userLists[1].add(recipe2);
		userLists[1].add(restaurant2);
				

		when(session.getAttribute("userLists")).thenReturn(userLists);
		when(request.getParameter("q")).thenReturn("Chicken");
		when(request.getParameter("n")).thenReturn("3");
		
		new ResultsPageServlet().service(request, response);
		
		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("resultsOrList"), ArgumentMatchers.eq("results"));

		
	}
	
	@Test
	public void testFromBackToSearch() throws Exception {

		when(session.getAttribute("userLists")).thenReturn(userLists);
		when(session.getAttribute("searchTerm")).thenReturn("Chicken");
		when(request.getParameter("resultCount")).thenReturn("3");
		
		new ResultsPageServlet().service(request, response);
		
		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("resultsOrList"), ArgumentMatchers.eq("results"));

		
	}
	
	@Test
	public void testNullValues() throws Exception {
		
		new ResultsPageServlet().service(request, response);
		
		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("resultsOrList"), ArgumentMatchers.eq("results"));
		
	}
	
	@Test
	public void testNullValues2() throws Exception {
		
		when(request.getParameter("n")).thenReturn("0");
		
		new ResultsPageServlet().service(request, response);
		
		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("resultsOrList"), ArgumentMatchers.eq("results"));
		
	}
	
}

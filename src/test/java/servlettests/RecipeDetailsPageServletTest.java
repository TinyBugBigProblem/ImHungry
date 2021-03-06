package servlettests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import data.UserList;
import servlets.RecipeDetailsPageServlet;
import servlets.RecipeDetailsPageServlet;

public class RecipeDetailsPageServletTest {

	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;
	
	Recipe[] results;
	UserList[] userLists;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		rd = mock(RequestDispatcher.class);
		
		when(request.getSession()).thenReturn(session);
		
		userLists = new UserList[3];
		for (int i = 0; i < 3; ++i) {
			userLists[i] = new UserList();
		}
		
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
		
		Recipe recipe1 = new Recipe(name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);
		Recipe recipe2 = new Recipe("Not" + name, pictureUrl, prepTime, cookTime, ingredients, instructions, rating);
        
		results = new Recipe[2];
		results[0] = recipe1;
		results[1] = recipe2;
        
        when(session.getAttribute("recipeResults")).thenReturn(results);
        when(request.getParameter("arrNum")).thenReturn("1");
        when(session.getAttribute("userLists")).thenReturn(userLists);
	}

	@Test
	public void testAddToFavorites() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");
		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[0].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExplore() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");
		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[2].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShow() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[1].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToFavoritesDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");

        userLists[1].add(results[1]);
        
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExploreDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");

        userLists[0].add(results[1]);
		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShowDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

        userLists[0].add(results[1]);

		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToFavoritesDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");

        userLists[2].add(results[1]);
        
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExploreDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");

        userLists[1].add(results[1]);
		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShowDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

        userLists[2].add(results[1]);

		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}	
	
	@Test
	public void testNoList() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn(null);
		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		verify(session, never()).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testIncorrectList() throws Exception {

        when(request.getRequestDispatcher("/jsp/recipeDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("a");

		
		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	
	
	@Test
	public void testExpiredSession() throws Exception {
		
        when(request.getRequestDispatcher("/jsp/search.jsp")).thenReturn(rd);
        when(session.getAttribute("recipeResults")).thenReturn(null);

		new RecipeDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);

	}


}

package servlettests;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import data.Restaurant;
import data.UserList;
import servlets.RestaurantDetailsPageServlet;
import servlets.RestaurantDetailsPageServlet;

public class RestaurantDetailsPageServletTest {


	@Mock
	HttpServletRequest request;
	@Mock
	HttpServletResponse response;
	@Mock
	HttpSession session;

	@Mock
	RequestDispatcher rd;
	
	Restaurant[] results;
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
		
        results = new Restaurant[2];
		results[0] = new Restaurant("A Good Restaurant", "https://www.mcdonalds.com/us/en-us.html", 1, "Everywhere", "(123)456-7890", 2.25, 5);
		results[1] = new Restaurant("A Bad Restaurant", "https://www.bk.com/", 2, "Almost everywhere", "(123)456-7896", 1.25, 50);
        
        when(session.getAttribute("restaurantResults")).thenReturn(results);
        when(request.getParameter("arrNum")).thenReturn("1");
        when(session.getAttribute("userLists")).thenReturn(userLists);
	}

	@Test
	public void testAddToFavorites() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");
		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[0].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExplore() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");
		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[2].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShow() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		userLists[1].add(results[1]);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToFavoritesDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");

        userLists[1].add(results[1]);
        
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExploreDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");

        userLists[0].add(results[1]);
		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShowDuplicate1() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

        userLists[0].add(results[1]);

		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToFavoritesDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("f");

        userLists[2].add(results[1]);
        
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToToExploreDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("t");

        userLists[1].add(results[1]);
		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testAddToDoNotShowDuplicate2() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("d");

        userLists[2].add(results[1]);

		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}	
	
	@Test
	public void testNoList() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn(null);
		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		verify(session, never()).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	@Test
	public void testIncorrectList() throws Exception {

        when(request.getRequestDispatcher("/jsp/restaurantDetails.jsp")).thenReturn(rd);
        when(request.getParameter("listType")).thenReturn("a");

		
		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);
		verify(session).setAttribute(ArgumentMatchers.eq("userLists"), ArgumentMatchers.eq(userLists));

	}
	
	
	
	@Test
	public void testExpiredSession() throws Exception {
		
        when(request.getRequestDispatcher("/jsp/search.jsp")).thenReturn(rd);
        when(session.getAttribute("restaurantResults")).thenReturn(null);

		new RestaurantDetailsPageServlet().service(request, response);

		verify(rd).forward(request, response);

	}

}

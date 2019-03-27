package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import api.AccessYelpAPI;
import api.GoogleImageSearch;
import api.Scrapper;
import data.Recipe;
import data.Restaurant;
import data.UserList;

/*
 * Back-end code for generating the Results Page
 */
@WebServlet("/results")
public class ResultsPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/*
	 * service method is invoked whenever user attempts to 
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.setAttribute("resultsOrList", "results");
		UserList[] userLists = (UserList[]) session.getAttribute("userLists");
		/*
		 * If no UserList array is stored in session, the server considers the user to be a new user,
		 *  and makes new userList array for this session. 
		 * Else, retrieve UserList array from session.
		 */
		if (userLists == null) {
			userLists = new UserList[3];
			for (int i = 0; i < 3; ++i) {
				userLists[i] = new UserList();
			}
			session.setAttribute("userLists", userLists);
		}
		UserList favoriteList = userLists[0];
		UserList doNotShowList = userLists[1];
		ArrayList<Restaurant> doNotShowRestaurants = doNotShowList.getRestaurants();
		ArrayList<Recipe> doNotShowRecipes = doNotShowList.getRecipes();
		
		// input validation should be done on front end (empty string, non-integer for resultCount, etc.)
		String searchTerm = request.getParameter("q");
		String resultCountRaw = request.getParameter("n");
		Integer resultCount = null;
		
		/*
		 *  If user clicked "return to search", get parameters from session.
		 * 	Else, get parameters from url
		 */
		if (searchTerm == null) {
			searchTerm = (String) session.getAttribute("searchTerm");
		}
		if (resultCountRaw == null) {
			resultCount = (Integer) session.getAttribute("resultCount");
		} else {
			resultCount = Integer.parseInt(resultCountRaw);
		}
	
		/* 
		 * Fetch a list of restaurant objects made from query results given by Yelp API
		 * Get enough results to make up for restaurants/recipes in Do Not Show list, which will not be displayed
		 */
		Vector<Restaurant> restaurants = AccessYelpAPI.YelpRestaurantSearch(searchTerm, resultCount + doNotShowRestaurants.size());
		/*
		 * Sort restaurants in ascending order of drive time from Tommy Trojan,
		 * using compareTo method overridden in Restaurant class
		 */
		Collections.sort(restaurants);
		Restaurant currRestaurant;
		int insertIndex = 0;
		// Check for restaurants in Favorite list, and put them on top
		for (int i = 0; i < restaurants.size(); ++i) {
			currRestaurant = restaurants.get(i);
			System.out.println(currRestaurant.getName());
			if (favoriteList.contains(currRestaurant)) {
				System.out.println("is in the favorite list");
				restaurants.remove(i);
				restaurants.add(insertIndex, currRestaurant);
				for (int j = 0; j < restaurants.size(); ++j) {
					System.out.print(restaurants.get(j).getName() + "  ");
				}
				System.out.println("");
				++insertIndex;
			}
		}
		// Check for restaurants in Do Not Show list, and remove them, 
		// 	assuming a restaurant cannot be in more than one predefined list
		for (int i = insertIndex; i < restaurants.size(); ++i) {
			currRestaurant = restaurants.get(i);
			if (doNotShowList.contains(currRestaurant)) {
				restaurants.remove(i);
				--i;
			}
		}
		/* 
		 * Fetch a list of recipe objects made by web scraping from allrecipes.com
		 */
		Vector<Recipe> recipes  = Scrapper.search(searchTerm, resultCount + doNotShowRecipes.size());
		/*
		 * Sort recipes in ascending order of prep time,
		 * using compareTo method overridden in Recipe class
		 */
		Collections.sort(recipes);
		Recipe currRecipe;
		insertIndex = 0;
		// Check for recipes in Favorite list, and put them on top
		for (int i = 0; i < recipes.size(); ++i) {
			currRecipe = recipes.get(i);
			System.out.println(currRecipe.getName());
			if (favoriteList.contains(currRecipe)) {
				System.out.println("is contained in favorite list");
				recipes.add(insertIndex, currRecipe);
				recipes.remove(i+1);
				++insertIndex;
			}
		}
		// Check for recipes in Do Not Show list, and remove them, 
		// 	assuming a recipe cannot be in more than one predefined list
		for (int i = insertIndex; i < recipes.size(); ++i) {
			currRecipe = recipes.get(i);
			if (doNotShowList.contains(currRecipe)) {
				recipes.remove(i);
				--i;
			}
		}
		// vector size should be resultCount (discard extra data)
		restaurants.setSize(resultCount);
		recipes.setSize(resultCount);
		// Make vectors into arrays and pass to jsp as attributes
		Restaurant[] restaurantArr = new Restaurant[resultCount];
		Recipe[] recipeArr = new Recipe[resultCount];
		restaurants.toArray(restaurantArr);
		recipes.toArray(recipeArr);

		// Google Image Search to make collage of images
		// array of image URLs passed to jsp as "imageUrlVec"
		Vector<String> imageUrlVec = GoogleImageSearch.GetImagesFromGoogle(searchTerm);
		String[] imageUrlArr = new String[imageUrlVec.size()];
		imageUrlVec.toArray(imageUrlArr);
		
		// Pass variables needed for generating front-end
		request.setAttribute("imageUrlVec", imageUrlArr);
		request.setAttribute("restaurantArr", restaurantArr);
		request.setAttribute("recipeArr", recipeArr);
		request.setAttribute("searchTerm", searchTerm);
		request.setAttribute("resultCount", resultCount);
		// store result arrays in session -> used for details page
		session.setAttribute("restaurantResults", restaurantArr);
		session.setAttribute("recipeResults", recipeArr);
		// store searchTerm and resultCount -> used when user clicks "Return to Search"
		session.setAttribute("searchTerm", searchTerm);
		session.setAttribute("resultCount", resultCount);
		
		RequestDispatcher dispatch = request.getRequestDispatcher("/jsp/results.jsp");
		dispatch.forward(request,  response);			
	}
}
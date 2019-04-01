package servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import data.*;

@WebServlet("/listManagement")
public class ListManagementPageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		UserList[] userLists = (UserList[]) session.getAttribute("userLists");
		if (userLists == null) { // Send to the search page if user lists haven't been created
			RequestDispatcher dispatch = request.getRequestDispatcher("/jsp/search.jsp");
			dispatch.forward(request,  response);
			return;
		}
		/*
		 * Implement pagination by sending starting index to jsp 
		 */
		if(request.getAttribute("page") == null) {
			request.setAttribute("index", 0);
			System.out.println("null, server, index = 0");
		}
		else {
			int index = Integer.parseInt( (String) request.getAttribute("page"));
			System.out.println("not null, server, index: " + index);
			request.setAttribute("index", index);
		}
		System.out.println("Server side index: " + request.getAttribute("page"));
		
		String listType = request.getParameter("listName");
//		System.out.println("ListType: " + listType.charAt(0));
		
		String op = request.getParameter("opType");
//		Check if user wants to move or remove an item from a list
		if (op != null) {
//			Get Variables to help move the item
			String recOrRest = request.getParameter("recOrRest");
			String sFromList = request.getParameter("fromList");
			int listNum = -1;
//			Map the letter name of list to an int
			if(sFromList.equals("f")) {
				listNum = 0;
			}
			else if(sFromList.equals("d")) {
				listNum = 1;
			}
			else {
				listNum = 2;
			}
//			Get position of item in question from current list
			int arrNum = Integer.parseInt(request.getParameter("arrNum"));
//			Get either recipe lists or restaurant lists from the Userlists in question
			UserList fromList = userLists[listNum];

			
			if(op.equals("r")) {
				if(recOrRest.equals("rec")) {
					// Get the array number for recipe from regular list
					int num = fromList.getArrayNum(fromList.getLists().get(arrNum).getRecipe());
					// Remove from regular list
					fromList.remove(fromList.getRecipes().get(num));
					// Remove from list management lists
					fromList.getLists().remove(arrNum);
				}
				else {
					// Get array number for restaurant from regular list
					int num = fromList.getArrayNum(fromList.getLists().get(arrNum).getRestaurant());
					// Remove from regular list
					fromList.remove(fromList.getRestaurants().get(num));
					// Remove from list management list
					fromList.getLists().remove(arrNum);
					
				}
			}else {
				
				// Calculate the new list value
				int toListNum = -1;
				if(op.equals("f")) {
					toListNum = 0;
				}
				else if(op.equals("d")) {
					toListNum = 1;
				}
				else if(op.equals("t")) {
						
					toListNum = 2;
				}
				else if(op.equals("up")) {
					toListNum = listNum;
				}
				else if(op.equals("down")) {
					toListNum = listNum;
				}
				else {
					toListNum = listNum;
				}
				UserList toList = userLists[toListNum];
				
				// Decide whether the data type is a Recipe or Restaurant.
				if(recOrRest.equals("rec")) {
					if(!op.equals("up") && !op.equals("down") && (!toList.contains(fromList.getRecipes().get(fromList.getArrayNum(fromList.getLists().get(arrNum).getRecipe()))))) { // When moving list item from one to another
						// Get recipe arrNum from original list
						int num = fromList.getArrayNum(fromList.getLists().get(arrNum).getRecipe());
						// Add recipe item to both lists
						toList.add(fromList.getRecipes().get(num));
						// Remove recipe item from previous original list
						fromList.remove(fromList.getRecipes().get(arrNum));
						// Remove recipe item from previous original list
						fromList.getLists().remove(arrNum);
					}
					// When it's possible to move an item up or down inside a list
					else{
						// Move item up list
						if(arrNum != 0 && op.equals("up")) {
							ListItem temp = fromList.getLists().get(arrNum - 1);
							fromList.getLists().set(arrNum-1, toList.getLists().get(arrNum));
							fromList.getLists().set(arrNum, temp);
						}
						// Move item down list
						else if(arrNum != fromList.getLists().size() - 1 && op.equals("down")) {
							ListItem temp = fromList.getLists().get(arrNum + 1);
							toList.getLists().set(arrNum+1, toList.getLists().get(arrNum));
							toList.getLists().set(arrNum, temp);
						}
					}
				}
				else {
					if(!op.equals("up") && !op.equals("down") && (!toList.contains(fromList.getRestaurants().get(fromList.getArrayNum(fromList.getLists().get(arrNum).getRestaurant()))))) {
						// Get recipe arrNum from original list
						int num = fromList.getArrayNum(fromList.getLists().get(arrNum).getRestaurant());
						// Add recipe item to new both lists
						toList.add(fromList.getRestaurants().get(num));
						// Remove recipe item from previous original list
						fromList.remove(fromList.getRestaurants().get(num));
						// Remove recipe item from previous original list
						fromList.getLists().remove(arrNum);
					}
					else{
						if(arrNum != 0 && op.equals("up")) {
							ListItem temp = fromList.getLists().get(arrNum - 1);
							fromList.getLists().set(arrNum-1, fromList.getLists().get(arrNum));
							fromList.getLists().set(arrNum, temp);	
						}
						else if(arrNum != fromList.getLists().size() - 1 && op.equals("down")) {
							ListItem temp = fromList.getLists().get(arrNum + 1);
							fromList.getLists().set(arrNum+1, toList.getLists().get(arrNum));
							fromList.getLists().set(arrNum, temp);
						}
					}
				}
			}
		}

		// Pass list to display to jsp
		if (listType != null) { // Check to see if the user wanted to go to another list
			switch (listType.charAt(0)) {
			case 'f': // User wants to go to favorites list
				request.setAttribute("listVal", userLists[0]); // Send the userList object that contains both restaurant and recipe files
				request.setAttribute("listName", "Favorites"); // Send the list name
				session.setAttribute("restaurants", userLists[0].getRestaurants()); // So that when user clicks on item, it shows in the details page
				session.setAttribute("recipes", userLists[0].getRecipes()); // Same as previous comment
				session.setAttribute("list", userLists[0].getLists());
				
				break;
			case 'd': // User wants to go to Do not Show list
				request.setAttribute("listVal", userLists[1]); // Send the userList object that contains both restaurant and recipe files
				request.setAttribute("listName", "Don't Show"); // Send the list name
				session.setAttribute("restaurants", userLists[1].getRestaurants()); // So that when user clicks on item, it shows in the details page
				session.setAttribute("recipes", userLists[1].getRecipes());
				session.setAttribute("list", userLists[1].getLists());
				
				break;
			case 't': // User wants to go to To Explore list
				request.setAttribute("listVal", userLists[2]); // Send the userList object that contains both restaurant and recipe files
				request.setAttribute("listName", "To Explore"); // Send the list name
				session.setAttribute("restaurants", userLists[2].getRestaurants()); // So that when user clicks on item, it shows in the details page
				session.setAttribute("recipes", userLists[2].getRecipes()); // Same as previous comment
				session.setAttribute("list", userLists[2].getLists());
				
				break;
			}			
		}
		/*
		 * Redirect back to list management page
		 */
		session.setAttribute("userLists", userLists); // Send the entire array of lists to session, so that we can access any item on front end 
		RequestDispatcher dispatch = request.getRequestDispatcher("/jsp/listManagement.jsp");
		dispatch.forward(request,  response);
	}

}

package data;
import java.io.Serializable;
import java.util.ArrayList;
/*
 * An UserList object will correspond to a predefined list (one of Favorite, Do Not Show, To Explore)
 * Total 3 UserList objects will be stored in session
 */
// Implements Serializable Interface to allow storing in session 
public class UserList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// ArrayList of restaurants and recipes in the predefined list
	private ArrayList<Restaurant> restaurants;
	private ArrayList<Recipe> recipes;
	private ArrayList<ListItem> lists;
	
	// Empty when the list is created (created when a new session is started)
	public UserList() {
		restaurants = new ArrayList<Restaurant>();
		recipes = new ArrayList<Recipe>();
		lists = new ArrayList<ListItem>();
	}
	
	public ArrayList<Restaurant> getRestaurants() {
		return restaurants;
	}

	public ArrayList<Recipe> getRecipes() {
		return recipes;
	}
	public ArrayList<ListItem> getLists(){
		return lists;
	}

	/*
	 * add methods:
	 * Returns false if the passed object is already in the list 
	 * Returns true if successfully added to the list
	 */
	public boolean add(Recipe r){
		if (recipes.contains(r)) {
			return false;
		}
		else {
			recipes.add(r);
			ListItem item = new ListItem(null, r, "rec");
			lists.add(item);
			return true;
		}
	}

	public boolean add(Restaurant r) {
		if (restaurants.contains(r)) {
			return false;
		}
		else {
			restaurants.add(r);
			ListItem item = new ListItem(r, null, "res");
			lists.add(item);
			return true;
		}
	}
	/*
	 * remove methods:
	 * Returns true if successfully removed from the list
	 * Returns false if the passed object is not in the list
	 */
	public boolean remove(Recipe r) {
		return recipes.remove(r);
	}
	
	public boolean remove(Restaurant r) {
		return restaurants.remove(r);
	}
	
	/*
	 * contains methods:
	 * Returns true if the passed object is in the list
	 * Returns false if the passed object is not in the list
	 */
	public boolean contains(Recipe r) {
		return recipes.contains(r);
	}
	
	public boolean contains(Restaurant r) {
		return restaurants.contains(r);
	}
	/*
	 * contains methods:
	 * Returns pos int if the passed object is in the list
	 * Returns -1 if the passed object is not in the list
	 */
	public int getArrayNum(Recipe r) {
		for(int i = 0; i < recipes.size(); ++i) {
			if(recipes.get(i).equals(r)) {
				return i;
			}
		}
		return -1;
	}
	public int getArrayNum(Restaurant r) {
		for(int i = 0; i < restaurants.size(); ++i) {
			if(restaurants.get(i).equals(r)) {
				return i;
			}
		}
		return -1;
	}
}

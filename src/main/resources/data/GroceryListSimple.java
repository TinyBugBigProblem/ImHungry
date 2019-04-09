package data;
import java.io.Serializable;
import java.util.ArrayList;
/*
 * An GroceryListSimple object will be a list of Strings, with a String recipe title
 * 1 GroceryListSimple object will be stored in session
 */
// Implements Serializable Interface to allow storing in session 
public class GroceryListSimple implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// Recipe title
	private String recipe;
	
	// ArrayList of Strings in the predefined list
	private ArrayList<String> groceries;
	
	// Empty when the list is created (created when a new session is started)
	public GroceryListSimple() {
		recipe = "";
		groceries = new ArrayList<String>();
	}
	
	public void setTitle(String t) {
		recipe = t;
	}
	
	public String getTitle() {
		return recipe;
	}
	
	public ArrayList<String> getGroceries() {
		return groceries;
	}
	
	/*
	 * add methods:
	 * Returns false if the passed object is already in the list 
	 * Returns true if successfully added to the list
	 * EDIT THIS LATER, SUCH THAT IF PREEXISTING ITEM EXISTS, ADD THE AMOUNTS
	 */
	public boolean add(String g){
		if (groceries.contains(g)) {
			return false;
		}
		else {
			groceries.add(g);
			return true;
		}
	}

	/*
	 * remove methods:
	 * Returns true if successfully removed from the list
	 * Returns false if the passed object is not in the list
	 */
	public boolean remove(String g) {
		return groceries.remove(g);
	}
	
	/*
	 * contains methods:
	 * Returns true if the passed object is in the list
	 * Returns false if the passed object is not in the list
	 */
	public boolean contains(String g) {
		return groceries.contains(g);
	}
	
	/*
	 * contains methods:
	 * Returns pos int if the passed object is in the list
	 * Returns -1 if the passed object is not in the list
	 */
	public int getArrayNum(String g) {
		for(int i = 0; i < groceries.size(); ++i) {
			if(groceries.get(i).equals(g)) {
				return i;
			}
		}
		return -1;
	}
}
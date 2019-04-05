package data;
import java.io.Serializable;
import java.util.ArrayList;
/*
 * An GroceryList object will be a list of GroceryItems
 * 1 GroceryList objects will be stored in session
 */
// Implements Serializable Interface to allow storing in session 
public class GroceryList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// ArrayList of GroceryItems in the predefined list
	private ArrayList<GroceryItem> groceries;
	
	// Empty when the list is created (created when a new session is started)
	public GroceryList() {
		groceries = new ArrayList<GroceryItem>();
	}
	
	public ArrayList<GroceryItem> getGroceries() {
		return groceries;
	}
	
	/*
	 * add methods:
	 * Returns false if the passed object is already in the list 
	 * Returns true if successfully added to the list
	 * EDIT THIS LATER, SUCH THAT IF PREEXISTING ITEM EXISTS, ADD THE AMOUNTS
	 */
	public boolean add(GroceryItem g){
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
	public boolean remove(GroceryItem g) {
		return groceries.remove(g);
	}
	
	/*
	 * contains methods:
	 * Returns true if the passed object is in the list
	 * Returns false if the passed object is not in the list
	 */
	public boolean contains(GroceryItem g) {
		return groceries.contains(g);
	}
	
	/*
	 * contains methods:
	 * Returns pos int if the passed object is in the list
	 * Returns -1 if the passed object is not in the list
	 */
	public int getArrayNum(GroceryItem g) {
		for(int i = 0; i < groceries.size(); ++i) {
			if(groceries.get(i).equals(g)) {
				return i;
			}
		}
		return -1;
	}
}

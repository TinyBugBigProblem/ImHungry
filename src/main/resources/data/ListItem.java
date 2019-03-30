package data;
import java.io.Serializable;

public class ListItem implements Serializable{
	private static final long serialVersionUID = 1L;

	private String type = null;
	private Restaurant resItem = null;
	private Recipe recItem = null;
	
	// Constructor
	public ListItem(Restaurant resItem, Recipe recItem, String type) {
		this.type = type;
		this.resItem = resItem;
		this.recItem = recItem;
	}
	/*
	 * Set the restaurant member variable 
	 */
	public void setRestaurant(Restaurant resItem) {
		this.type = "res";
		this.resItem = resItem;
		this.recItem = null;
	}
	/*
	 * Set the recipe member variable 
	 */
	public void setRecipe(Recipe recItem) {
		this.type = "rec";
		this.recItem = recItem;
		this.resItem = null;
	}
	/*
	 * Get the restaurant member variable 
	 */
	public Restaurant getRestaurant() {
		return resItem;
	}
	/*
	 * Get the recipe item 
	 */
	public Recipe getRecipe() {
		return recItem;
	}
	/*
	 * Return type of list item 
	 */
	public String getType() {
		return type;
	}
}

package data;
import java.io.Serializable;

public class GroceryItem implements Serializable{
	private static final long serialVersionUID = 1L;

	private String name = null;
	private double amount = 0;
	private String units = null;
	private String qualifier = null;
	private double cost = 0;
	
	// Constructor
	public GroceryItem(String name, double amount, String units) {
		this.name = name;
		this.amount = amount;
		this.units = units;
	}
	/*
	 * Returns the name of the grocery item
	 */
	public String getName() {
		return name;
	}
	/*
	 * Returns the amount of the grocery item
	 */
	public double getAmount() {
		return amount;
	}
	/*
	 * Returns the units used for the grocery item
	 */
	public String getUnits() {
		return units;
	}
}

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
	public GroceryItem(String name, double amount, String units, String qualifier, double cost) {
		this.name = name;
		this.amount = amount;
		this.units = units;
		this.qualifier = qualifier;
		this.cost = cost;
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
	/*
	 * Returns the units used for the grocery item
	 */
	public String getQualifier() {
		return qualifier;
	}
	/*
	 * Returns the units used for the grocery item
	 */
	public double getCost() {
		return cost;
	}
	/*
	 * Equals overridden for vector::contains() method
	 */
	public boolean equals(Object obj) {
		if (obj instanceof GroceryItem) {
			GroceryItem o = (GroceryItem) obj;
			return (this.name.equals(o.name));
		}
		else {
			return false;
		}
	}
}

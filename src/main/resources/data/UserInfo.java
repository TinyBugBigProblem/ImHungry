package data;
import java.io.Serializable;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username = null;
	private String password = null;

	// Constructor
	public UserInfo(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/*
	 * Returns username
	 */
	public String getUsername() {
		return this.username;
	}
	
	/*
	 * Returns password
	 */
	public String getPassword() {
		return this.password;
	}

}

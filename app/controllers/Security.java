package controllers;

public class Security extends Secure.Security {

	/**
	 * This method is called during the authentication process. This is where you check if
	 * the user is allowed to log in into the system. This is the actual authentication process
	 * against a third party system (most of the time a DB).
	 *
	 * @param username
	 * @param password
	 * @return true if the authentication process succeeded
	 */
	static boolean authenticate(String username, String password) {

		return true;

	}

}

package controllers;

import models.User;
import play.Logger;
import play.mvc.Controller;

public class Login extends Controller {

	public static void index() {

		//TODO DEBUG
		User user = User.find("byUsername", "admin").first();
		//TODO DEBUG

		Logger.debug("Logged in as user "+user);
		session.put("username", user.username);

		Interviewer.index();
	}

	
}

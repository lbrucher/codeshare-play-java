package controllers;

import play.*;
import play.mvc.*;
import utils.AppFixtures;

import java.util.*;

import models.*;

public class SecuredController extends Controller {

	@Before
    static void verifyAuthenticated()
	{
		// Get current username
        String username = session.get("username");
//Logger.debug("username="+username);
        if (username == null) {
			if (Play.mode == Play.Mode.DEV) {
				username = "admin";
				session.put("username", username);
			} else {
        	Login.view();
			}
		}

        // Check user actually exists
        User currentUser = User.find("byUsername", username).first();
//Logger.debug("user="+currentUser);
        if (currentUser == null)
        	Login.view();

        // Store current user in context
        renderArgs.put("currentUser", currentUser);
    }
}
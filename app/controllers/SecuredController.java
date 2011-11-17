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
        if (username == null) {

			//TODO DEV -- auto login as admin...
			if (Play.mode.isDev()) {
				username = "admin";
				session.put("username", username);
			} else {
	        	Login.index();
				return;
			}
		}

        // Check user actually exists
        User currentUser = User.find("byUsername", username).first();
        if (currentUser == null)
        	Login.index();

        // Store current user in context
        renderArgs.put("currentUser", currentUser);
    }
}
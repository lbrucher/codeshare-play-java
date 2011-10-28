package controllers;

import play.*;
import play.mvc.*;
import utils.AppFixtures;

import java.util.*;

import models.*;

public class Interviewer extends SecuredController {

	public static void view() {
        render();
    }

	public static void viewSession(int id) {
		// verify session id
		//TODO...

		renderArgs.put("sessionId", id);
		render();
	}
}

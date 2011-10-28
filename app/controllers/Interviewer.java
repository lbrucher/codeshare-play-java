package controllers;

import play.Logger;

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


	public static void sessionInit(int id) {
		Logger.debug("YIHEE:"+id);
	}
}

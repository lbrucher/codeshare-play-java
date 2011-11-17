package controllers;

import models.InterviewSession;
import models.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import utils.json.InterviewSession1;
import utils.json.InterviewSessions;

import java.util.Date;
import java.util.List;

@With(Secure.class)
public class Interviewer extends Controller {

	private static int codeGen = 1;

	@Before(priority=0)
	static void autoLoginUsersInDevMode()
	{
		if (Play.mode.isDev()) {
			if (!Security.isConnected()) {
				Logger.debug("Auto login 'admin'");
				session.put("username", "admin");
			}
		}
	}

//	@Before
//	static void populateRenderArgs()
//	{
//		// Store current user in context
////		renderArgs.put("currentUser", currentUser);
//	}

	public static void index() {
        render();
    }


	public static void resumeSession(int code) {

		User loggedinUser = getCurrentUser();

		// Verify the code is correct
		List<InterviewSession> sessions = InterviewSession.find("byCode", code).fetch();
		if (sessions.size() != 1) {
			if (sessions.size() == 0)
				Logger.debug("Requested session with unknown code <"+code+">");
			else
				// this should not happen!
				Logger.error("There are <"+sessions.size()+" sessions with code <"+code+">!!");
			index();
			return;
		}

		// Verify the current user has access to this session
		InterviewSession session = sessions.get(0);
		if (session.user.id != loggedinUser.id) {
			//TODO should we raise the log level?
			if (Logger.isDebugEnabled())
				Logger.debug("Requested session with code <"+code+"> and belonging to user <"+session.user.username+"> does not belong to logged in user <"+loggedinUser.username+">!");
			index();
			return;
		}

		// all good, render the session page
		renderArgs.put("session_code", code);
		render();
	}




	/**
	 * Return lists of open and closed sessions
	 * For each session, return: code, creationDate
	 */
	public static void listSessions() {
		renderJSON(
			new InterviewSessions(InterviewSession.find("byUser", getCurrentUser()).<InterviewSession>fetch())
		);
	}

	
	/**
	 * Create a new interview session
	 */
	public static void createSession() {
		InterviewSession is = new InterviewSession();

		is.code = generateCode();
		is.creationDate = new Date();
		is.isOpen = true;
		is.candidateText = "";
		is.candidateSeenLastInterviewerText = false;
		is.interviewerText = "";
		is.interviewerComments = "";
		is.interviewerSeenLastCandidateText = false;
		is.user = getCurrentUser();

		is.save();

//		renderJSON( object2map(is, new String[]{"code","creationDate","isOpen"}) );
		renderJSON( new InterviewSession1(is) );
	}


	public static void initSession(int code ) {

	}

	/**
	 *
	 * @return a new code for an InterviewSession
	 */
	protected static int generateCode() {
		return codeGen++;
	}


	/**
	 *
	 * @return
	 */
	protected static User getCurrentUser() {
		if (!Security.isConnected())
			return null;
		return (User) User.find("byUsername", Security.connected()).fetch().get(0);
	}
}

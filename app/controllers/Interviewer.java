package controllers;

import models.InterviewSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import utils.json.InterviewSession1;

import java.util.Date;

public class Interviewer extends SecuredController {

	private static int codeGen = 1;

	public static void view() {
        render();
    }

	public static void viewSession(int id) {
		// verify session id
		//TODO...

		renderArgs.put("sessionId", id);
		render();
	}


	/**
	 * Return lists of open and closed sessions
	 * For each session, return: code, creationDate
	 */
	public static void sessionList() {
/*
		// List of fields from the InterviewSession model we need to return
		final String[] desiredFields = new String[]{"code", "creationDate"};

		// Transform an InterviewSession into a map with selected fields only
		Transformer t = new Transformer() {
			public Object transform(Object o) { return object2map(o, desiredFields); }
		};

		// Build the resulting map
		Map<String,Object> results = new HashMap<String, Object>();
		results.put("openSessions", CollectionUtils.collect(InterviewSession.find("byIsOpen", Boolean.TRUE).fetch(), t));
		results.put("closedSessions", CollectionUtils.collect(InterviewSession.find("byIsOpen", Boolean.FALSE).fetch(), t));

		renderJSON(results);
*/
		renderJSON(CollectionUtils.collect(InterviewSession.findAll(), new Transformer() {
			public Object transform(Object o) {
				return new InterviewSession1((InterviewSession) o);
			}
		}));
	}


	
	/**
	 * Create a new interview session
	 */
	public static void sessionCreate() {
		InterviewSession is = new InterviewSession();

		is.code = generateCode();
		is.creationDate = new Date();
		is.isOpen = true;
		is.candidateText = "";
		is.candidateSeenLastInterviewerText = false;
		is.interviewerText = "";
		is.interviewerComments = "";
		is.interviewerSeenLastCandidateText = false;

		is.save();

//		renderJSON( object2map(is, new String[]{"code","creationDate","isOpen"}) );
		renderJSON( new InterviewSession1(is) );
	}


	/**
	 *
	 * @return a new code for an InterviewSession
	 */
	protected static int generateCode() {
		return codeGen++;
	}

}

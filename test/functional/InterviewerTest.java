package functional;

import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.junit.Before;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Router;
import play.test.*;
import utils.json.InterviewSession1;
import utils.json.InterviewSessions;

public class InterviewerTest extends FunctionalTest {

	private Gson gson;

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test-data.yml");		
		gson = new Gson();
	}


	@Test
    public void sessionListTest() {
        Http.Response response = GET("/interviewer/list");
        assertStatus(200, response);

		InterviewSessions sessions = gson.fromJson(response.out.toString(), InterviewSessions.class);

		// ensure correct number of sessions
		assertEquals(1, sessions.openSessions.size());
		assertEquals(1, sessions.closedSessions.size());

		// ensure correct number of OPEN sessions
		assertEquals(sessions.openSessions.size(),
				CollectionUtils.countMatches(sessions.openSessions, new Predicate() {
					public boolean evaluate(Object o) { return ((InterviewSession1) o).isOpen; }
				})
		);

		// ensure correct number of CLOSED sessions
		assertEquals(sessions.closedSessions.size(),
				CollectionUtils.countMatches(sessions.closedSessions, new Predicate() {
					public boolean evaluate(Object o) { return !((InterviewSession1)o).isOpen; }
				})
		);
    }


	@Test
    public void sessionCreateTest() {
		Http.Response response;

		// first get the list of sessions and store the number of them
		response = GET("/interviewer/list");
		assertStatus(200, response);
		InterviewSessions sessions = gson.fromJson(response.out.toString(), InterviewSessions.class);

		// Create the new session
		response = POST("/interviewer");
        assertStatus(200, response);
		InterviewSession1 is = gson.fromJson(response.out.toString(), InterviewSession1.class);
		assertNotNull(is);

		// request the list again and ensure there is one more session
		response = GET("/interviewer/list");
		assertStatus(200, response);
		InterviewSessions newSessions = gson.fromJson(response.out.toString(), InterviewSessions.class);

		// ensure we've got one more open session
		assertEquals(sessions.openSessions.size()+1, newSessions.openSessions.size());

		// ensure we've still the same number of closed sessions
		assertEquals(sessions.closedSessions.size(), newSessions.closedSessions.size());

		final int expectedCode = is.code;
		assertEquals(1, CollectionUtils.countMatches(newSessions.openSessions, new Predicate() {
			public boolean evaluate(Object o) {
				return ((InterviewSession1) o).code == expectedCode;
			}
		}));
    }


	@Test
    public void resumeValidSessionTest() {
		Http.Response response;

		// Create a new session
		response = POST("/interviewer");
        assertStatus(200, response);
		InterviewSession1 session = gson.fromJson(response.out.toString(), InterviewSession1.class);
		assertNotNull(session);

		// request the newly created session
		response = GET("/interviewer/"+session.code);
		assertStatus(200, response);
//TODO assert the content of the response?
    }

	@Test
    public void resumeNonExistingSessionTest() {
		Http.Response response;

		// request the list again and ensure there is one more session
		response = GET("/interviewer/0000");
		assertStatus(302, response);
//TODO check we're redirected to the right page
    }

	@Test
    public void resumeWrongUserSessionTest() {
		Http.Response response;

		// request the list again and ensure there is one more session
		response = GET("/interviewer/2000");
		assertStatus(302, response);
//TODO check we're redirected to the right page
//		assertHeaderEquals("Location", Router.getFullUrl("Interviewer.index()"), response);
    }

}

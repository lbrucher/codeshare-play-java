package functional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hibernate.type.ListType;
import org.junit.Before;
import org.junit.Test;
import play.Logger;
import play.mvc.Http;
import play.test.FunctionalTest;
import utils.json.InterviewSession1;

import java.util.Arrays;
import java.util.List;


public class Interviewer extends FunctionalTest {

	private Gson gson;

	@Before
	public void setUp() {
		gson = new Gson();
	}


	@Test
    public void sessionInitTest() {
        Http.Response response = GET("/interviewer/session/list");
        assertStatus(200, response);

		List<InterviewSession1> sessions = Arrays.asList(gson.fromJson(response.out.toString(), InterviewSession1[].class));

		assertEquals(2, sessions.size());
		assertEquals(1, CollectionUtils.countMatches(sessions, new Predicate() {
			public boolean evaluate(Object o) {
				return ((InterviewSession1) o).isOpen;
			}
		}));
		assertEquals(1, CollectionUtils.countMatches(sessions, new Predicate() {
			public boolean evaluate(Object o) { return !((InterviewSession1)o).isOpen; }
		}));

    }


	@Test
    public void sessioncreateTest() {
		Http.Response response;

		// first get the list of sessions and store the number of them
		response = GET("/interviewer/session/list");
		assertStatus(200, response);
		int numSessions = gson.fromJson(response.out.toString(), InterviewSession1[].class).length;

		// Create the new session
		response = POST("/interviewer/session/create");
        assertStatus(200, response);
		InterviewSession1 is = gson.fromJson(response.out.toString(), InterviewSession1.class);
		assertNotNull(is);

		// request the list again and ensure there is one more session
		response = GET("/interviewer/session/list");
		assertStatus(200, response);
		int newNumSessions = gson.fromJson(response.out.toString(), InterviewSession1[].class).length;

		assertEquals(numSessions+1, newNumSessions);
    }

}

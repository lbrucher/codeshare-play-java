package unit;

import com.google.gson.Gson;
import models.InterviewSession;
import models.User;
import org.junit.Before;
import org.junit.Test;
import play.test.Fixtures;
import play.test.UnitTest;
import utils.json.InterviewSessions;

import java.util.List;


public class InterviewSessions1TransformerTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteDatabase();
		Fixtures.loadModels("test-data.yml");
	}

    @Test
    public void simple() {

		List<InterviewSession> sessions = InterviewSession.all().fetch();
		assertEquals(3, sessions.size());

		InterviewSessions x = new InterviewSessions(sessions);
		assertEquals(2, x.openSessions.size());
		assertEquals(1, x.closedSessions.size());
	}

}

package utils;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {

//		// #IF DEV
//		if (!AppFixtures.initialized)
//			AppFixtures.initialize();
		
        // Check if the user database is empty
        if(User.count() == 0) {
            Fixtures.loadModels("initial-users.yml");
        }

        // Check if the sessions database is empty
		if (InterviewSession.count() == 0) {
			Fixtures.loadModels("initial-sessions.yml");
		}

    }
 
}

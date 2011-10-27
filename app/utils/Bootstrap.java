package utils;
import play.*;
import play.jobs.*;
import play.test.*;
 
import models.*;
 
@OnApplicationStart
public class Bootstrap extends Job {
 
    public void doJob() {

//		// #IF DEV
//		if (!AppFixtures.initialized)
//			AppFixtures.initialize();
		
        // Check if the database is empty
        if(User.count() == 0) {
            Fixtures.loadModels("../test/test-data.yml");
        }

    }
 
}

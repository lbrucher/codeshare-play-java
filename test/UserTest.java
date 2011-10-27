import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class UserTest extends UnitTest {

    @Test
    public void createAndRetrieveUser() {
    	new User("joe", "secret", "Joe Smith").save();
        
        User joe = User.find("byUsername", "joe").first();
        
        // Test 
        assertNotNull(joe);
        assertEquals("Joe Smith", joe.fullname);
    }

    @Test
    public void login() {
        // Create a new user and save it
    	new User("joe", "secret", "Joe Smith").save();
        
        // Test 
        assertNotNull(User.authenticate("joe", "secret"));
        assertNull(User.authenticate("joe", "badpassword"));
        assertNull(User.authenticate("tom", "secret"));
    }
}

package controllers;

import models.InterviewSession;
import models.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import play.Logger;
import play.db.Model;
import utils.json.InterviewSession1;

import javax.persistence.Entity;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

public class Interviewer extends SecuredController {

	private static int codeGen = 1;

	public static void index() {
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
		renderJSON( CollectionUtils.collect(InterviewSession.findAll(), new Transformer() {
			public Object transform(Object o) { return new InterviewSession1((InterviewSession)o); }
		}) );
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


	/**
	 * Generate a map out of the given Object with only the fields mentioned as the 2nd arg
	 * @param o
	 * @param desiredFields
	 * @return
	 */
/*
	protected static Map<String, Object> object2map( Object o, String[] desiredFields ) {
		Map<String,Object> map = new HashMap<String, Object>();

		List<String> desiredProps = Arrays.asList(desiredFields);
		Field[] props = o.getClass().getDeclaredFields();
		for (Field prop : props) {
			if (desiredProps.contains(prop.getName())) {
				try {
					map.put(prop.getName(), prop.get(o));
				} catch (IllegalAccessException e) {
					Logger.info("Illegal access exception for field <"+prop.getName()+">");
				}
			}
		}

		return map;
	}
*/
	
/*
	public static void test() {
		Map<String,String> m = new HashMap<String, String>();
		m.put("id1", "Joe");
		m.put("id2", "Fred");
		m.put("id3", "Arthur");

		renderJSON(m);
	}

	public static void test2() {
		renderJSON( new Integer(304) );
	}


	public static class Person { //extends play.db.jpa.Model {
		public String name;
		public String address;
		public Person(String n, String a) { name=n; address=a;}
	}

	public static void test3() {
		List<Person> p = new ArrayList<Person>();
		p.add(new Person("Joe2", "Broadway"));
		p.add(new Person("Fred", "Canal St"));

		renderJSON(p);
	}

	public static void test4() {
		User u = new User("joe", "secret", "Joe Smith");
		renderJSON(u);
	}

	public static void test5() {
		renderJSON(new Person("Joe", "Broadway"));
	}
*/
	
}

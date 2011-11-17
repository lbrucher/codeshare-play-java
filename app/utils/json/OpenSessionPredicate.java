package utils.json;

import models.InterviewSession;
import org.apache.commons.collections.Predicate;

public class OpenSessionPredicate implements Predicate {
	public boolean evaluate(Object o) {
		return ((InterviewSession)o).isOpen;
	}
}

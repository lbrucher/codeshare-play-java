package utils.json;

import models.InterviewSession;
import org.apache.commons.collections.Transformer;

public class InterviewSessions1Transformer implements Transformer {
	public Object transform(Object o) {
		if (o instanceof InterviewSession)
			return new InterviewSession1( (InterviewSession)o );
		else
			return null;
	}
}

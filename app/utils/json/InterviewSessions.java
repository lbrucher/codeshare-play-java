package utils.json;

import models.InterviewSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class InterviewSessions {
	public List<InterviewSession1> openSessions;
	public List<InterviewSession1> closedSessions;

	public InterviewSessions(List<InterviewSession> sessions) {

		Transformer tx = new InterviewSessions1Transformer();
		Predicate pdct = new OpenSessionPredicate();

		this.openSessions = (List<InterviewSession1>) CollectionUtils.collect(
					CollectionUtils.select(sessions, pdct), tx);

		this.closedSessions = (List<InterviewSession1>) CollectionUtils.collect(
					CollectionUtils.select(sessions, PredicateUtils.notPredicate(pdct)), tx);
	}
}

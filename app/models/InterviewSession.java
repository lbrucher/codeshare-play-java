package models;

import play.db.jpa.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;


@Entity
public class InterviewSession extends Model {

	public int code;
	public Date creationDate;
	public boolean isOpen;
	public String interviewerText;
	public String interviewerComments;
	public boolean interviewerSeenLastCandidateText;
	public String candidateText;
	public boolean candidateSeenLastInterviewerText;

	@ManyToOne(optional=false)
	public User user;

	public String toString() {
		return "<"+code+":"+(isOpen?"open":"closed")+">";
	}
}

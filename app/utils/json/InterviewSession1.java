package utils.json;

import models.InterviewSession;

import java.util.Date;


public class InterviewSession1 {
	public int code;
	public Date creationDate;
	public boolean isOpen;

	public InterviewSession1(InterviewSession is) {
		this.code = is.code;
		this.creationDate = is.creationDate;
		this.isOpen = is.isOpen;
	}
}

function BackendServices() {

	var self = this;

	//
	// Session object = {id:<int>, status:<string>, createdOn:<date>}
	//
	this.idGen = 1;

	this.openSessions = [
		  { id:self.idGen++, open:true, createdOn:new Date(2011,10,01), interviewerText:'interviewer text', lastInterviewerUpdateTime:null, candidateText:'candidate text', lastCandidateUpdateTime:null, interviewerComments:'no comments' }
		, { id:self.idGen++, open:true, createdOn:new Date(2011,10,03), interviewerText:'', lastInterviewerUpdateTime:null, candidateText:'', lastCandidateUpdateTime:null, interviewerComments:'' }
	];

	this.closedSessions = [
		  { id:self.idGen++, open:false, createdOn:new Date(2011,09,12,4,50,32), interviewerText:'some int text', lastInterviewerUpdateTime:null, candidateText:'some candidate text', lastCandidateUpdateTime:null, interviewerComments:'blah blah blah' }
	];


	// callback(openSessions, closedSessions)
	//     openSessions,
	//	   closedSessions = [ <session object> ]
	//
	this.initInterviewerMainView = function(callback) {
		setTimeout( function() { callback(self.openSessions, self.closedSessions) }, 10 );
	};


	// callback(newSession)
	//		newSession = <session object>
	//
	this.createSession = function(callback) {
		callback({ id:self.idGen++, status:'open', createdOn:new Date() });
	};



	this.getVersion = function() {
		return "1.0-dev";
	};


	this.findSession = function(id) {
		for(var i=0; i<this.openSessions.length;i++)
			if (this.openSessions[i].id == id)
				return this.openSessions[i];
		for(var i=0; i<this.closedSessions.length;i++)
			if (this.closedSessions[i].id == id)
				return this.closedSessions[i];
		return null;
	}

	this.interviewerSessionServices = {
		// callback(success,myText,otherText,lastOtherUpdateTime,commentsText)
		initSession: function(sessionId, callback) {
			var s = self.findSession(sessionId);
			if (s == null || !s.open) {
				console.log((s==null ? 'unknown session #<%i>':'session #<%i> is not open'), sessionId);
				callback(false);
			}
			else {
				console.log('Initialized session #<%i>', sessionId);
				s.lastCandidateUpdateTime = new Date();
				callback(true, s.interviewerText, s.candidateText, s.lastCandidateUpdateTime, s.interviewerComments);
			}
		},

		// callback(success,sessionOpen,lastOtherUpdateTime,otherText)
		refreshOtherText: function(sessionId, lastOtherUpdateTime, callback) {
			var s = self.findSession(sessionId);
			if (s == null) {
				console.log('refreshOtherText failed for session #<%i>', sessionId);
				callback(false);
			}
			else {
				console.log('refreshOtherText successful for session #<%i>', sessionId);
				s.lastCandidateUpdateTime = new Date();
				callback(true, s.open, s.lastCandidateUpdateTime, s.candidateText);
			}
		},

		// callback(success,sessionOpen,lastOtherUpdateTime,otherText)
		updateMyText: function(sessionId, newText, lastOtherUpdateTime, callback) {
			var s = self.findSession(sessionId);
			if (s == null) {
				console.log('updateMyText failed for session #<%i>', sessionId);
				callback(false);
			}
			else {
				console.log('updateMyText successful for session #<%i>', sessionId);
				s.interviewerText = newText;
				s.lastCandidateUpdateTime = new Date();
				callback(true, s.open, s.lastCandidateUpdateTime, s.candidateText);
			}
		},

		// callback(success)
		updateMyComments: function(sessionId, newText, callback) {
			var s = self.findSession(sessionId);
			if (s == null) {
				console.log('updateMyComments failed for session #<%i>', sessionId);
				callback(false);
			}
			else {
				console.log('updateMyComments successful for session #<%i>', sessionId);
				s.interviewerComments = newText;
				callback(true);
			}
		}
	};


	this.candidateSessionServices = {
		// callback(success,myText,otherText,lastOtherUpdateTime)
		initSession: function(sessionId) {
			var s = self.findSession(sessionId);
			if (s == null || !s.open)
				callback(false);
			else {
				s.lastInterviewerUpdateTime = new Date();
				callback(true, s.candidateText, s.interviewerText, s.lastInterviewerUpdateTime);
			}
		},

		// callback(success,sessionOpen,lastOtherUpdateTime,otherText)
		refreshOtherText: function(sessionId, lastOtherUpdateTime, callback) {
			var s = self.findSession(sessionId);
			if (s == null)
				callback(false);
			else {
				s.lastInterviewerUpdateTime = new Date();
				callback(true, s.open, s.lastInterviewerUpdateTime, s.interviewerText);
			}
		},

		// callback(success,sessionOpen,lastOtherUpdateTime,otherText)
		updateMyText: function(sessionId, newText, lastOtherUpdateTime, callback) {
			var s = self.findSession(sessionId);
			if (s == null)
				callback(false);
			else {
				s.candidateText = newText;
				s.lastInterviewerUpdateTime = new Date();
				callback(true, s.open, s.lastInterviewerUpdateTime, s.interviewerText);
			}
		},
	};
}

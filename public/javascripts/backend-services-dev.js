function BackendServices() {

	var self = this;

	//
	// Session object = {id:<int>, status:<string>, createdOn:<date>}
	//
	this.idGen = 1;

	this.openSessions = [
		  { id:self.idGen++, status:'open', createdOn:new Date(2011,10,01) }
		, { id:self.idGen++, status:'open', createdOn:new Date(2011,10,03) }
	];

	this.closedSessions = [
		  { id:self.idGen++, status:'closed', createdOn:new Date(2011,09,12,4,50,32) }
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
}

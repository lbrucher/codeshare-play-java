function BackendServices() {

	// Return:
	//   {
	//     openSessions: [ {id:<int>, status:<string>, createdOn:<date>}, ... ],
	//	   closedSessions: [ {id:<int>, status:<string>, createdOn:<date>}, ... ]
	//   }
	//
	this.initInterviewerMainView = function(callback) {
		$.get('listSessions', function(data, status) {

alert(data);
//		callback(openSessions, closedSessions);

		});
	};


	this.getVersion = function() {
		return "1.0";
	};
}

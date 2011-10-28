// ======================================================================================
// TEXTAREA TRACKER
// ======================================================================================
function TextareaTracker(_elTA, _textChangedCallback)
{
	this.elTA = _elTA;
	this.textChangedCallback = _textChangedCallback;

	// gets a timeout to keep track of changes in the TA.
	// textChangedCallback() gets invoked when that timeout expires.
	this.changeTracker = null;

	// last know content of the TA
	this.text = null;


	var self=this;
	this.elTA.onkeydown = function(e) {self.ta_onkeydown(e);};
	this.elTA.onkeyup = function(e) {self.ta_textChanged(e);};
}

// Enables tabbing in text area
// Credit goes to See[Mike]Code, http://i.seemikecode.com
TextareaTracker.prototype.ta_onkeydown = function(evt)
{
	var e = window.event || evt;
	var k = e.keyCode ? e.keyCode : e.charCode ? e.charCode : e.which;
	if (k == 9) {
		var t = e.target ? e.target : e.srcElement ? e.srcElement : e.which;
		if (t.setSelectionRange)
		{	// chrome 1.0...
			e.preventDefault();
			var ss = t.selectionStart;
			var se = t.selectionEnd;
			var pre = t.value.slice(0, ss);
			var post = t.value.slice(se, t.value.length);
			t.value = pre + "\t" + post;
			t.selectionStart = ss + 1;
			t.selectionEnd = t.selectionStart;
		}
		else
		{	// ie6...
			e.returnValue = false;
			var r = document.selection.createRange();
			r.text = "\t";
			r.setEndPoint("StartToEnd", r);
			r.select();
		}
	}
}

TextareaTracker.prototype.ta_textChanged = function(evt)
{
	var e = window.event || evt;
	var t = e.target ? e.target : e.srcElement ? e.srcElement : e.which;

	this.onTextChanged(t.value);
}

TextareaTracker.prototype.setText = function(newText)
{
	var firstTime = this.text == null;
	this.elTA.value = newText;
	if (!firstTime)
		this.onTextChanged(this.elTA.value);
}

TextareaTracker.prototype.onTextChanged = function(newText)
{
	if (this.text == newText)
		return;

	if (this.changeTracker != null)
		clearTimeout(this.changeTracker);

	var self = this;
	this.text = newText;
	this.changeTracker = setTimeout( function() { self.textChangedCallback(self.text) }, 1000);
}


// ======================================================================================
// CODESHARE
// ======================================================================================

function Codeshare(_sessionId, _elMyText, _elOtherText, _elCommentsText)
{
	this.sessionId = _sessionId;
	this.backendServices = null;

	this.lastOtherUpdateTime = null;

	var self = this;
	this.myTextTracker = new TextareaTracker(_elMyText, function(text) {self.updateMyText(text);});
	this.elOtherText = _elOtherText;
	this.otherUpdater = null;
	this.myCommentsTracker = _elCommentsText ? new TextareaTracker(_elCommentsText, function(text) {self.updateMyComments(text)}) : null;

	this.log = function(msg) { console.log(msg); };
}

Codeshare.prototype.start = function(_backendServices)
{
	this.backendServices = _backendServices;

	var self = this;
	this.backendServices.initSession(this.sessionId, function(success,myText,otherText,lastOtherUpdateTime,commentsText) {
		if (success) {
			self.myTextTracker.setText(myText);

			self.elOtherText.value = otherText;
			self.lastOtherUpdateTime = lastOtherUpdateTime;

			if (self.myCommentsTracker)
				self.myCommentsTracker.setText(commentsText);
		}
		else
			self.log("initSession failed!");

		self.setOtherUpdater();
	});
}

Codeshare.prototype.stop = function()
{
	this.unsetOtherUpdater();
}

Codeshare.prototype.setOtherUpdater = function()
{
	this.unsetOtherUpdater();
	var self = this;
	this.otherUpdater = setTimeout( function() { self.refreshOtherText() }, 4000);
}

Codeshare.prototype.unsetOtherUpdater = function()
{
	if (this.otherUpdater != null)
		clearTimeout(this.otherUpdater);
	this.otherUpdater = null;
}

Codeshare.prototype.updateOtherText = function(sessionOpen,lastOtherUpdateTime,otherText)
{
	if (!sessionOpen)
	{
		window.location.href = this.urlSessionClosed;
		return;
	}

	if (otherText != null)
	{
		this.lastOtherUpdateTime = lastOtherUpdateTime;
		this.elOtherText.value = otherText;
	}
}

Codeshare.prototype.refreshOtherText = function()
{
	try
	{
		this.log("Requesting other text update...");
		var self = this;

		this.backendServices.refreshOtherText(this.sessionId, this.lastOtherUpdateTime,
			function(success, sessionOpen,lastOtherUpdateTime,otherText) {
				if (success)
					self.updateOtherText(sessionOpen,lastOtherUpdateTime,otherText);
				else
					self.log("UpdateOtherText failed");
				self.setOtherUpdater();
		});
/*
		var ts = new Date().getTime();

		$.get(this.urlRefreshOtherText+'/'+self.lastOtherUpdateTime,
			  { ts:ts }
			, function(data, status) {
				if (status == 'success')
					self.updateOtherText(data);
				else
					self.log("UpdateOtherText: status:["+status+"]");
				self.setOtherUpdater();
			  }
			, "json"
		);
*/

	}
	catch(e)
	{
		this.setOtherUpdater();
	}
}


Codeshare.prototype.updateMyText = function(newText)
{
	this.unsetOtherUpdater();

	try
	{
		this.log("Sending updated my text...");
		var self = this;

		this.backendServices.updateMyText(this.sessionId, newText, this.lastOtherUpdateTime,
			function(success, sessionOpen,lastOtherUpdateTime,otherText) {
				if (success)
					self.updateOtherText(sessionOpen,lastOtherUpdateTime,otherText);
				else
					self.log("UpdateMyText failed");
				self.setOtherUpdater();
		});

/*
		$.post(this.urlUpdateMyText,
			{
				myText:newText,
				lastOtherUpdateTime:self.lastOtherUpdateTime
			},
			function(data, status) {
				if (status == 'success')
					self.updateOtherText(data);
				else
					self.log("UpdateMyText: status:["+status+"]");
				self.setOtherUpdater();
			},
			"json"
		);
*/
	}
	catch(e)
	{
		this.setOtherUpdater();
	}
}


Codeshare.prototype.updateMyComments = function(newText)
{
	try
	{
		this.log("Sending updated comments...");
		var self = this;

		this.backendServices.updateMyComments(this.sessionId, newText, function(success) {
			if (!success)
				self.log("UpdateMyComments failed");
		});

/*
		$.post(this.urlUpdateMyComments,
			{
				myComments:newText
			},
			function(data, status) {
				if (status != 'success')
					self.log("UpdateMyComments: status:["+status+"]");
			},
			"json"
		);
*/
	}
	catch(e)
	{
	}
}

/*
 * Refresh the current content of 'myText', and send the update
 */
Codeshare.prototype.setMyText = function(newText)
{
	this.myTextTracker.setText(newText);
}



function TerminalSession() {

	this.doAction = function(command) {
		document.HtmlEmulation.TerminalCommand.value = command;
		document.HtmlEmulation.submit();
	};
}


var TerminalContext = new function() {
	//this.keyboardMappings = new TerminalKeyboardMappings();
};

var terminalSession = new TerminalSession();
//TerminalContext.addKeyboardMappings(new TerminalKeyboardMapping(
//		AdditionalKeys.NONE, KeyCodes.ENTER, functionElement));

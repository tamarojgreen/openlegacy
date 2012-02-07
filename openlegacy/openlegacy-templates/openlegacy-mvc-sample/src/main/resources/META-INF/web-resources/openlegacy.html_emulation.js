function TerminalSession() {

	this.doAction = function(command) {
		document.HtmlEmulation.TerminalCommand.value = command;
		document.HtmlEmulation.submit();
	};
}

var terminalSession = new TerminalSession();

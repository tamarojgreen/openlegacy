function TerminalSession() {

	this.doAction = function(keyboardKey) {
		document.HtmlEmulation.KeyboardKey.value = keyboardKey;
		document.HtmlEmulation.submit();
	};
}

var terminalSession = new TerminalSession();
